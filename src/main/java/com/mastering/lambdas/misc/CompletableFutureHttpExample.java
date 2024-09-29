package com.mastering.lambdas.misc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

@Slf4j
class CompletableFutureFileReadExample {

    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    // Simulated I/O-bound operation: reading data from a file
    private static List<String> readDataFromFile(String filePath) {
        List<String> strings = List.of();
        try {
            // Simulate a delay in reading file
            Thread.sleep(2000);
            return Files.readAllLines(Paths.get(filePath));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
            log.error("Thread was interrupted during file read operation", e);
            return strings;
        } catch (IOException e) {
            log.error("I/O error occurred while reading the file: {}", filePath, e);
            return strings;
        } catch (Exception e) {
            log.error("Unexpected error occurred while reading the file: {}", filePath, e);
            return strings;
        }
    }

    // Processing combined data
    private static String processData(List<String> data1, List<String> data2, List<String> data3) {
        return String.format("Combined Data: %d lines from Source 1, %d lines from Source 2, %d lines from Source 3",
                data1.size(), data2.size(), data3.size());
    }

    public static void main(String[] args) {
        // File paths (ensure these files exist for testing)
        String filePath1 = "src/main/java/com/mastering/lambdas/misc/data1.txt";
        String filePath2 = "src/main/java/com/mastering/lambdas/misc/data2.txt";
        String filePath3 = "src/main/java/com/mastering/lambdas/misc/data3.txt";

        // Sequential Execution
        long startSequential = System.currentTimeMillis();
        List<String> data1 = readDataFromFile(filePath1);
        List<String> data2 = readDataFromFile(filePath2);
        List<String> data3 = readDataFromFile(filePath3);
        String resultSequential = processData(data1, data2, data3);
        long endSequential = System.currentTimeMillis();

        System.out.println("Sequential Execution:");
        System.out.println(resultSequential);
        System.out.println("Total time (sequential): " + (endSequential - startSequential) + " ms");

        // Asynchronous Execution
        long startAsync = System.currentTimeMillis();

        CompletableFuture<List<String>> future1 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath1), executor);
        CompletableFuture<List<String>> future2 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath2), executor);
        CompletableFuture<List<String>> future3 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath3), executor);

        CompletableFuture<String> combinedFuture = future1
                .thenCombine(future2, (d1, d2) -> processData(d1, d2, future3.join()));

        String resultAsync = combinedFuture.join(); // Wait for the combined result
        long endAsync = System.currentTimeMillis();

        System.out.println("\nAsynchronous Execution:");
        System.out.println(resultAsync);
        System.out.println("Total time (asynchronous): " + (endAsync - startAsync) + " ms");

/*        // Asynchronous Execution - Version 2
        long startAsync = System.currentTimeMillis();

        CompletableFuture<List<String>> future1 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath1), executor);
        CompletableFuture<List<String>> future2 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath2), executor);
        CompletableFuture<List<String>> future3 = CompletableFuture.supplyAsync(() -> readDataFromFile(filePath3), executor);

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        combinedFuture.join(); // Wait for the combined result
        long endAsync = System.currentTimeMillis();

        System.out.println("\nAsynchronous Execution:");
        System.out.println(processData(future1.join(), future2.join(), future3.join()));
        System.out.println("Total time (asynchronous): " + (endAsync - startAsync) + " ms");*/

        executor.shutdown(); // Shut down the executor
    }
}
