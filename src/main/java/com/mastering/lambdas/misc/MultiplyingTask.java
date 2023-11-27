package com.mastering.lambdas.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiplyingTask implements Callable {
    int a;
    int b;
    long sleepTime;
    String taskName;

    public MultiplyingTask(String taskName, int a, int b, long sleepTime) {
        this.taskName = taskName;
        this.a = a;
        this.b = b;
        this.sleepTime = sleepTime;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Started taskName: " + taskName);
        int result = a * b;
        Thread.sleep(sleepTime);
        System.out.println("Completed taskName: " + taskName);
        return result;
    }
}

class ExecutionCompletionServiceaMain {

/*  ExecutorService executorService = Executors.newFixedThreadPool(4);
    List<Future> futures = new ArrayList<Future<Integer>>();
    futures.add(executorService.submit(A));
    futures.add(executorService.submit(B));
    futures.add(executorService.submit(C));
    futures.add(executorService.submit(D));

    Then we can iterate over the list to get the computed result of each future:
    for (Future future:futures) {
        Integer result = future.get();
        // rest of the code here.
    }

    Now the similar functionality can also be achieved using ExecutorCompletionService as:
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    CompletionService executorCompletionService= new ExecutorCompletionService<>(executorService );
    Then again we can submit the tasks and get the result like:
    List<Future> futures = new ArrayList<Future<Integer>>();
    futures.add(executorCompletionService.submit(A));
    futures.add(executorCompletionService.submit(B));
    futures.add(executorCompletionService.submit(C));
    futures.add(executorCompletionService.submit(D));

    for (int i=0; i < futures.size(); i++) {
        Integer result = executorCompletionService.take().get();
        // Some processing here
    }
    Suppose task B finished first followed by task C. But task A was still going on.
    In that case when using ExecutorService the for loop would be waiting for the result of
    task A to be available. So in case of ExecutorService tasks will be processed in the same
    order in which they were submitted.
    But in later case the tasks will be processed in order the result becomes available, the order tasks are completed.*/
    public static void main(String[] args) {
        MultiplyingTask multiplyingTask1 = new MultiplyingTask("Task 1", 10, 20, 2000l);
        MultiplyingTask multiplyingTask2 = new MultiplyingTask("Task 2", 30, 40, 4000l);
        MultiplyingTask multiplyingTask3 = new MultiplyingTask("Task 3", 40, 50, 3000l);
        MultiplyingTask multiplyingTask4 = new MultiplyingTask("Task 4", 50, 60, 1000l);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(executorService);
        List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
        futures.add(executorCompletionService.submit(multiplyingTask1));
        futures.add(executorCompletionService.submit(multiplyingTask2));
        futures.add(executorCompletionService.submit(multiplyingTask3));
        futures.add(executorCompletionService.submit(multiplyingTask4));

/*        futures.add(executorService.submit(multiplyingTask1));
        futures.add(executorService.submit(multiplyingTask2));
        futures.add(executorService.submit(multiplyingTask3));
        futures.add(executorService.submit(multiplyingTask4));*/

/*        for (Future<Integer> future : futures) {
            try {
                Integer result = future.get();
                System.out.println("Result: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }*/


/*        You might be wondering why we need an extra counter? Unfortunately ExecutorCompletionService
        doesn't tell you how many Future objects are still there waiting so you must remember how many
        times to call take().*/
        for (int i = 0; i < futures.size(); i++) {
            try {
                Integer result = executorCompletionService.take().get();
                System.out.println("Result: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10)
                .thenApplyAsync(result -> result * 2)
                .thenApplyAsync(result -> result + 5);

        future.thenAccept(System.out::println);
    }
}
