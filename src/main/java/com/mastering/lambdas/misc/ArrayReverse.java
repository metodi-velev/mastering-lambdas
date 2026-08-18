package com.mastering.lambdas.misc;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayReverse {

    // Method to reverse the elements of an array using Streams
    public static int[] reverseArrayWithStreams(int[] arr) {
        return IntStream.range(0, arr.length)
                .map(i -> arr[arr.length - 1 - i])  // Map each index to the reverse
                .toArray();  // Collect the result into an array
    }

    // Using the in-place two-pointer approach:
    // It's about as efficient as possible: O(n) time and O(1) auxiliary space.
    public static int[] reverseArrayInPlace(int[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }

    /**
     * Returns a new reversed array using Streams.
     * Works with String[], Integer[], Double[], etc.
     */
    public static <T> T[] reverseArrayWithStreams(T[] arr) {
        return IntStream.range(0, arr.length)
                .mapToObj(i -> arr[arr.length - 1 - i])  // Map each index to the reverse
                .toArray(size -> Arrays.copyOf(arr, size));  // Collect the result into an array
    }

    /**
     * Reverses an object array in place.
     * Works with String[], Integer[], Double[], etc.
     */
    public static <T> T[] reverseArrayInPlace(T[] arr) {
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            T tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        return arr;
    }

    // Main method to test the reverseArrayWithStreams method
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};

        System.out.println("Original array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }

        // Reverse the array using Streams
        int[] reversedArr = reverseArrayWithStreams(arr);

        System.out.println("\nReversed array:");
        for (int num : reversedArr) {
            System.out.print(num + " ");
        }
    }
}