package com.mastering.lambdas.misc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VenkatCFSample {

    private static final Executor executor = Executors.newFixedThreadPool(7, (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread t1 = new Thread(() -> System.out.println("Is a new thread created by the main thread daemon by default?"));

        t1.start();
        //t1 is not daemon since it is spawned from the main thread and inherits its non-daemon behavior from it
        System.out.println("Is daemon? " + t1.isDaemon());

        System.out.println("In main " + Thread.currentThread().getName());

        CompletableFuture<Integer> future = create();

        sleep(100);

        //get() is a blocking call. Don't use get() -> use for example thenAccept() instead
        //System.out.println(future.get());     // bad style
        future.thenAccept(VenkatCFSample::printIt);


        System.out.println("Main thread output " + Thread.currentThread().getName());

        sleep(3000);
    }

    private static void printIt(Integer data) {
        System.out.println(data + " : " + Thread.currentThread().getName());
    }

    private static int compute() {
        System.out.println("Thread of execution: " + Thread.currentThread().getName());
        sleep(2000);
        return 2;
    }

    public static CompletableFuture<Integer> create() {
        return CompletableFuture.supplyAsync(VenkatCFSample::compute, executor);
    }

    private static void computePipeline(CompletableFuture<Integer> future) throws InterruptedException {
        future
                .thenApply(data -> data * 2)
                .thenApply(data -> data + 1)
                .thenAccept(System.out::println);

        Thread.sleep(5000);
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
