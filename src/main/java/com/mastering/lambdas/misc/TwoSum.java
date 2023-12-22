package com.mastering.lambdas.misc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    public static void main(String[] args) {
        boolean result1 = isTwoSumEqualToTen(new Integer[]{11, 90, 4, 1, 15, 70, 10, 20, 9}, 100);  //90+10=100
        System.out.println("Result of Two Sum to one hundred is: " + result1);

        boolean result2 = isTwoSumEqualToTen(new Integer[]{11, 92, 4, 1, 15, 70, 10, 20, 9}, 100);  //No two numbers add to 100
        System.out.println("Result of Two Sum to one hundred is: " + result2);
    }

    public static boolean isTwoSumEqualToTen(Integer[] array, int summand) {

        //Map<Integer, Boolean> map = Arrays.stream(array).distinct().collect(toMap(Integer::valueOf, e -> true));
        Map<Integer, Boolean> map = new HashMap<>();    //Use HashMap to achieve O(N) time complexity
        System.out.println("With anyMatch");            //Space complexity is O(N)
        return Arrays.stream(array).peek(e -> map.put(e, true)).anyMatch(e -> map.containsKey(summand - e));
    }

}
