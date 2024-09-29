package com.mastering.lambdas.misc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

class CompletableFutureAPICallsExample {

    private static final HttpClient client = HttpClient.newHttpClient();

    // Method to perform a network call
    private static CompletableFuture<String> fetchUserData(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public static void main(String[] args) {
        String userUrl = "https://jsonplaceholder.typicode.com/users/1";
        String dogUrl = "https://dog.ceo/api/breeds/image/random";

        // Sequential Execution
        long startSequential = System.currentTimeMillis();
        String userResponse = fetchUserData(userUrl).join();
        String dogResponse = fetchUserData(dogUrl).join();
        long endSequential = System.currentTimeMillis();

        System.out.println("Sequential Execution:");
        System.out.println("User Data: " + userResponse);
        System.out.println("Dog Data: " + dogResponse);
        System.out.println("Total time (sequential): " + (endSequential - startSequential) + " ms");

        // Asynchronous Execution
        long startAsync = System.currentTimeMillis();
        CompletableFuture<String> userFuture = fetchUserData(userUrl);
        CompletableFuture<String> dogFuture = fetchUserData(dogUrl);

        // Wait for all futures to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(userFuture, dogFuture);
        allFutures.join(); // Wait for all to complete

        long endAsync = System.currentTimeMillis();

        System.out.println("\nAsynchronous Execution:");
        System.out.println("User Data: " + userFuture.join());
        System.out.println("Dog Data: " + dogFuture.join());
        System.out.println("Total time (asynchronous): " + (endAsync - startAsync) + " ms");
    }
}

class CompletableFutureDataProcessingExample {

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    // Simulated I/O-bound operation: fetching data from a database
    private static String fetchDataFromSource1() {
        try {
            TimeUnit.SECONDS.sleep(2); // Simulate 2 seconds of delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data from Source 1";
    }

    // Simulated I/O-bound operation: fetching data from another database
    private static String fetchDataFromSource2() {
        try {
            TimeUnit.SECONDS.sleep(3); // Simulate 3 seconds of delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data from Source 2";
    }

    // Processing data from both sources
    private static String processData(String data1, String data2) {
        return data1 + " & " + data2; // Combine the data
    }

    public static void main(String[] args) {
        // Sequential Execution
        long startSequential = System.currentTimeMillis();
        String data1 = fetchDataFromSource1();
        String data2 = fetchDataFromSource2();
        String resultSequential = processData(data1, data2);
        long endSequential = System.currentTimeMillis();

        System.out.println("Sequential Execution:");
        System.out.println("Result: " + resultSequential);
        System.out.println("Total time (sequential): " + (endSequential - startSequential) + " ms");

        // Asynchronous Execution
        long startAsync = System.currentTimeMillis();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(CompletableFutureDataProcessingExample::fetchDataFromSource1, executor);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(CompletableFutureDataProcessingExample::fetchDataFromSource2, executor);

        // Combine results when both futures complete
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, CompletableFutureDataProcessingExample::processData);

        // Wait for the combined result
        String resultAsync = combinedFuture.join();
        long endAsync = System.currentTimeMillis();

        System.out.println("\nAsynchronous Execution:");
        System.out.println("Result: " + resultAsync);
        System.out.println("Total time (asynchronous): " + (endAsync - startAsync) + " ms");

        executor.shutdown(); // Shut down the executor
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

class CompletableFutureThenCombineExample {

    // Simulated long-running task 1 (e.g., fetching data from a service)
    private static String task1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return "Task1 Result";
    }

    // Simulated long-running task 2 (e.g., fetching data from a database)
    private static String task2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "Task2 Result";
    }

    // Sequential execution of tasks
    private static void sequentialExecution() throws InterruptedException {
        long start = System.currentTimeMillis();

        String result1 = task1();
        String result2 = task2();

        String combinedResult = result1 + " and " + result2;
        long end = System.currentTimeMillis();

        System.out.println("Sequential Combined Result: " + combinedResult);
        System.out.println("Sequential Execution Time: " + (end - start) + " ms");
    }

    // Asynchronous execution of tasks using CompletableFuture with thenCombine
    private static void completableFutureExecution() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                return task1();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                return task2();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        // Combine both futures when complete
        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (result1, result2) -> result1 + " and " + result2);

        String combinedResult = combinedFuture.get();
        long end = System.currentTimeMillis();

        System.out.println("Async Combined Result: " + combinedResult);
        System.out.println("Asynchronous Execution Time: " + (end - start) + " ms");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Sequential Execution
        sequentialExecution();

        // Asynchronous Execution with CompletableFuture
        completableFutureExecution();
    }
}

class ECommerceExample {

    // Simulated service call to fetch product details from a remote API
    private static String fetchProductDetails() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3); // Simulate delay
        return "Product Details: [Name: Laptop, Category: Electronics]";
    }

    // Simulated service call to fetch pricing details from another service
    private static String fetchPricingInfo() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4); // Simulate delay
        return "Pricing Info: [Price: $1200, Discount: 10%]";
    }

    // Sequential execution of fetching product details and pricing info
    private static void sequentialExecution() throws InterruptedException {
        long start = System.currentTimeMillis();

        String productDetails = fetchProductDetails();
        String pricingInfo = fetchPricingInfo();

        String result = productDetails + " | " + pricingInfo;
        long end = System.currentTimeMillis();

        System.out.println("Sequential Result: " + result);
        System.out.println("Sequential Execution Time: " + (end - start) + " ms");
    }

    // Asynchronous execution using CompletableFuture with thenCombine
    private static void completableFutureExecution() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        CompletableFuture<String> productDetailsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchProductDetails();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> pricingInfoFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchPricingInfo();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        // Combine both futures when they complete
        CompletableFuture<String> combinedFuture = productDetailsFuture.thenCombine(pricingInfoFuture,
                (productDetails, pricingInfo) -> productDetails + " | " + pricingInfo);

        String result = combinedFuture.get();
        long end = System.currentTimeMillis();

        System.out.println("Async Result: " + result);
        System.out.println("Asynchronous Execution Time: " + (end - start) + " ms");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Sequential Execution
        sequentialExecution();

        // Asynchronous Execution with CompletableFuture
        completableFutureExecution();
    }
}

class ECommerceComplexExample {

    // Simulated call to fetch basic product info from a remote API
    private static String fetchProductInfo() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Simulate delay
        return "Product Info: [Name: Laptop, Description: High-end gaming laptop]";
    }

    // Simulated call to fetch pricing info from a pricing service
    private static String fetchPricingInfo() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3); // Simulate delay
        return "Pricing Info: [Price: $1500, Tax: $100, Discount: 15%]";
    }

    // Simulated call to fetch stock availability from a warehouse service
    private static String fetchStockInfo() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1); // Simulate delay
        return "Stock Info: [Available in 10 units]";
    }

    // Simulated call to fetch user reviews from a review service
    private static String fetchUserReviews() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4); // Simulate delay
        return "User Reviews: [Rating: 4.8/5, 120 reviews]";
    }

    // Simulated call to fetch related products using a recommendation engine
    private static String fetchRelatedProducts() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Simulate delay
        return "Related Products: [Mouse, Keyboard, Headset]";
    }

    // Sequential execution of all service calls
    private static void sequentialExecution() throws InterruptedException {
        long start = System.currentTimeMillis();

        String productInfo = fetchProductInfo();
        String pricingInfo = fetchPricingInfo();
        String stockInfo = fetchStockInfo();
        String userReviews = fetchUserReviews();
        String relatedProducts = fetchRelatedProducts();

        String result = productInfo + " | " + pricingInfo + " | " + stockInfo + " | " + userReviews + " | " + relatedProducts;
        long end = System.currentTimeMillis();

        System.out.println("Sequential Result: " + result);
        System.out.println("Sequential Execution Time: " + (end - start) + " ms");
    }

    private static String printCombinedString(String accumulated, String current) {
        return accumulated + " | " + current;
    }

    // Asynchronous execution using CompletableFuture and thenCombine
    private static void completableFutureExecution() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        CompletableFuture<String> productInfoFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchProductInfo();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> pricingInfoFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchPricingInfo();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> stockInfoFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchStockInfo();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> userReviewsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchUserReviews();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> relatedProductsFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return fetchRelatedProducts();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

/*        // Combining all futures into one final result
        CompletableFuture<String> combinedFuture = productInfoFuture
                .thenCombine(pricingInfoFuture, (productInfo, pricingInfo) -> productInfo + " | " + pricingInfo)
                .thenCombine(stockInfoFuture, (previous, stockInfo) -> previous + " | " + stockInfo)
                .thenCombine(userReviewsFuture, (previous, userReviews) -> previous + " | " + userReviews)
                .thenCombine(relatedProductsFuture, (previous, relatedProducts) -> previous + " | " + relatedProducts);*/

        // Combining all futures into one final result - Version 2 using method reference
        CompletableFuture<String> combinedFuture = productInfoFuture
                .thenCombine(pricingInfoFuture, ECommerceComplexExample::printCombinedString)
                .thenCombine(stockInfoFuture, ECommerceComplexExample::printCombinedString)
                .thenCombine(userReviewsFuture, ECommerceComplexExample::printCombinedString)
                .thenCombine(relatedProductsFuture, ECommerceComplexExample::printCombinedString);

        // Get the final result
        String result = combinedFuture.get();
        long end = System.currentTimeMillis();

        System.out.println("Async Result: " + result);
        System.out.println("Asynchronous Execution Time: " + (end - start) + " ms");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Sequential Execution
        System.out.println("Running sequential execution...");
        sequentialExecution();

        // Asynchronous Execution with CompletableFuture
        System.out.println("Running asynchronous execution...");
        completableFutureExecution();
    }
}

class LogFileProcessingExample {

    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    // Simulated method to read a log file and process its content
    private static List<String> readLogFile(String filePath) throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(2); // Simulate file read delay
        Path path = Paths.get(filePath);
        return Files.readAllLines(path)
                .stream()
                .filter(line -> line.contains("ERROR")) // Simulate processing (e.g., filtering errors)
                .collect(Collectors.toList());
    }

    // Sequential execution of log file processing
    private static void sequentialExecution(List<String> filePaths) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();

        StringBuilder finalReport = new StringBuilder();

        for (String filePath : filePaths) {
            List<String> fileContent = readLogFile(filePath); // Read and process each file sequentially
            finalReport.append(String.join("\n", fileContent)).append("\n");
        }

        long end = System.currentTimeMillis();
        System.out.println("Sequential Report:");
        System.out.println(finalReport);
        System.out.println("Sequential Execution Time: " + (end - start) + " ms");
    }

    // Asynchronous execution using CompletableFuture and thenCombine
    private static void completableFutureExecution(List<String> filePaths) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        // Create a CompletableFuture for each file
        CompletableFuture<String> finalFuture = filePaths.stream()
                .map(filePath -> CompletableFuture.supplyAsync(() -> {
                    try {
                        List<String> fileContent = readLogFile(filePath);
                        return String.join("\n", fileContent);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                // Use thenCombine to combine all futures
                .reduce((future1, future2) -> future1.thenCombine(future2, (content1, content2) -> content1 + "\n" + content2))
                .orElse(CompletableFuture.completedFuture("")); // In case of an empty file list

        // Get the final combined report
        String finalReport = finalFuture.get();

        long end = System.currentTimeMillis();
        System.out.println("Asynchronous Report:");
        System.out.println(finalReport);
        System.out.println("Asynchronous Execution Time: " + (end - start) + " ms");
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // List of file paths to process
        List<String> filePaths = List.of(
                "src/main/java/com/mastering/lambdas/misc/server1.log",
                "src/main/java/com/mastering/lambdas/misc/server2.log",
                "src/main/java/com/mastering/lambdas/misc/server3.log"
        );

        // Sequential Execution
        System.out.println("Running sequential execution...");
        sequentialExecution(filePaths);

        // Asynchronous Execution
        System.out.println("Running asynchronous execution...");
        completableFutureExecution(filePaths);

        executor.shutdown();
    }
}