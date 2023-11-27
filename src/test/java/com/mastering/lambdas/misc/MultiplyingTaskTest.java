package com.mastering.lambdas.misc;

import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
class MultiplyingTaskTest {

    private static final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    private final Executor executor = Executors.newFixedThreadPool(7, (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    @AfterAll
    public static void closeClient() throws IOException {
        asyncHttpClient.close();
    }

    @Test
    public void either() throws Exception {
        final CompletableFuture<String> java = questions("java");
        final CompletableFuture<String> scala = questions("scala");

        final CompletableFuture<String> both = java.
                applyToEither(scala, String::toUpperCase);

        both.thenAcceptAsync(title -> log.debug("format: \"First: {}\"", title), executor);
    }

    @Test
    public void thenApplyTest() throws Exception {
        CompletableFuture<String> cfResult = questions("java")
                .thenApply(r1 -> r1.concat("a1"))
                .thenApply(r2 -> r2.concat("a2"))
                .thenApply(r3 -> r3.concat("a3"))
                .thenApply(r3 -> r3.concat(null))
                .exceptionally(throwable -> "sorry, try again later")
                .whenComplete((a, b) -> System.out.println("DONE"));

        System.out.println(cfResult.join());

    }

    @Test
    public void asyncHttpWithCallbacks() throws Exception {
        loadTag("java", response -> log.debug("Got: {}", response), throwable -> log.error("Mayday!", throwable));
        TimeUnit.SECONDS.sleep(5);
        System.out.println("End");
    }

    private CompletableFuture<String> questions(String language) {
        return CompletableFuture.supplyAsync(() -> language, executor);
    }

    //Callback using blocking API
    public void loadTag(String tag, Consumer<String> onSuccess, Consumer<Throwable> onError) {
        asyncHttpClient
                .prepareGet("https://stackoverflow.com/questions/tagged/" + tag)
                .execute(
                        new AsyncCompletionHandler<Void>() {

                            @Override
                            public Void onCompleted(Response response) {
                                onSuccess.accept(response.getResponseBody());
                                return null;
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                log.error("Mayday!", t);
                                onError.accept(t);
                            }
                        }
                );
    }

    @Test
    public void asyncHttpWithCallbacksWithCompletableFuture() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        loadTagCF("java").get();
        System.out.println("End");
    }

    //Callback using non-blocking CompletableFuture
    public CompletableFuture<String> loadTagCF(String tag) {
        final CompletableFuture<String> promise = new CompletableFuture<>();
        asyncHttpClient
                .prepareGet("https://stackoverflow.com/questions/tagged/" + tag)
                .execute(
                        new AsyncCompletionHandler<Void>() {

                            @Override
                            public Void onCompleted(Response response) {
                                log.debug("Got: {}", response.getResponseBody());
                                promise.complete(response.getResponseBody());
                                return null;
                            }

                            @Override
                            public void onThrowable(Throwable t) {
                                log.error("Mayday!", t);
                                promise.completeExceptionally(t);
                            }
                        }
                );
        return promise;
    }

}