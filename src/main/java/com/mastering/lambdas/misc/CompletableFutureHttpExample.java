package com.mastering.lambdas.misc;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureHttpExample {

    private static final HttpClient client = HttpClient.newHttpClient();

    // Method to perform a network call
    private static CompletableFuture<String> fetchData(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public static void main(String[] args) {
        String url1 = "https://jsonplaceholder.typicode.com/posts/1";
        String url2 = "https://jsonplaceholder.typicode.com/posts/2";
        String url3 = "https://jsonplaceholder.typicode.com/posts/3";

        // Sequential Execution
        long startSequential = System.currentTimeMillis();
        String response1 = fetchData(url1).join();
        String response2 = fetchData(url2).join();
        String response3 = fetchData(url3).join();
        long endSequential = System.currentTimeMillis();

        System.out.println("Sequential Execution:");
        System.out.println(response1);
        System.out.println(response2);
        System.out.println(response3);
        System.out.println("Total time (sequential): " + (endSequential - startSequential) + " ms");

        // Asynchronous Execution
        long startAsync = System.currentTimeMillis();
        CompletableFuture<String> future1 = fetchData(url1);
        CompletableFuture<String> future2 = fetchData(url2);
        CompletableFuture<String> future3 = fetchData(url3);

        // Wait for all futures to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);
        allFutures.join(); // Wait for all to complete

        long endAsync = System.currentTimeMillis();

        System.out.println("\nAsynchronous Execution:");
        System.out.println(future1.join());
        System.out.println(future2.join());
        System.out.println(future3.join());
        System.out.println("Total time (asynchronous): " + (endAsync - startAsync) + " ms");
    }
}