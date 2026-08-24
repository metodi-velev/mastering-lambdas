package com.mastering.lambdas.misc;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MergeAndSortAndRemoveDuplicatesFromTwoIntArraysUsingStreams {
    public static void main(String[] args) {
        int[] array1 = {10, 1, 145, 3, 89, 12, 6, 19};
        int[] array2 = {3, 2, 1, 4, 5, 6};

        int[] sortedArray = IntStream.concat(Arrays.stream(array1), Arrays.stream(array2)).distinct().sorted().toArray();
        System.out.println("Sorted Arrays with distinct elements: " + Arrays.toString(sortedArray));
    }
}
