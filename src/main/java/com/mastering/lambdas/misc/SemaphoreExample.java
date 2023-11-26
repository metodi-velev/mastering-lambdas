package com.mastering.lambdas.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * We can use semaphores to limit the number of concurrent threads accessing a specific resource.
 */
class SemaphoreExample {

    private Semaphore semaphore;

    public SemaphoreExample(int slotLimit) {
        semaphore = new Semaphore(slotLimit);
    }

    boolean tryLogin() {
        System.out.println(semaphore.availablePermits());
        return semaphore.tryAcquire();
    }

    void logout() {
        semaphore.release();
    }

    int availableSlots() {
        return semaphore.availablePermits();
    }

    public static void main(String[] args) {
        int slots = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(slots);
        SemaphoreExample loginQueue = new SemaphoreExample(slots);
        IntStream.range(0, slots)
                .forEach(user -> executorService.execute(loginQueue::tryLogin));
        executorService.shutdown();

        System.out.println("Available slots before logout: " + loginQueue.availableSlots());
        System.out.println(loginQueue.tryLogin());
        loginQueue.logout();
        System.out.println("Available slots after logout: " + loginQueue.availableSlots());
        System.out.println(loginQueue.tryLogin());
        System.out.println("Available slots after login: " + loginQueue.availableSlots());
    }

}
