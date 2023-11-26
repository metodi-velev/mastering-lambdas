package com.mastering.lambdas.misc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ArrayListSize {
    public static int findCapacity(ArrayList<Integer> list) throws IllegalAccessException, NoSuchFieldException {
        Field field = ArrayList.class.getDeclaredField("elementData");
        field.setAccessible(true);
        return ((Object[]) field.get(list)).length;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ArrayList<Integer> list = new ArrayList<>();
        IntStream.rangeClosed(0, 164).forEach(list::add);
        System.out.println("Size:: " + list.size() + " Capacity:: " + findCapacity(list));
        IntStream.rangeClosed(0, 164).forEach(i -> System.out.println(list.get(i)));
    }
}

class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> IntStream.rangeClosed(0, 164).forEach(i -> System.out.println(i + " : " + Thread.currentThread().getName() + " : " + Thread.currentThread().getState())));
        Thread thread2 = new Thread(() -> IntStream.rangeClosed(0, 164).forEach(i -> System.out.println(i + " : " + Thread.currentThread().getName() + " : " + Thread.currentThread().getState())));
        thread1.setName("Thread 1");
        thread2.setName("Thread 2");
        new Thread(() -> System.out.println("Active Threads: " + Thread.activeCount())).start();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        IntStream.rangeClosed(0, 164).forEach(i -> System.out.println(i + " : " + Thread.currentThread().getName() + " : " + Thread.currentThread().getState()));
    }
}
