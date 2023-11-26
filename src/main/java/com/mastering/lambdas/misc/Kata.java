package com.mastering.lambdas.misc;


import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kata {
    public static int[] digitize(long n) {
        String str = String.valueOf(n);
        String[] atrArr = str.split("");
        int[] result = new int[atrArr.length];

/*        for (int i = atrArr.length - 1; i >= 0; i--) {
            result[atrArr.length - 1 - i] = Integer.parseInt(atrArr[i]);
        }*/

/*        IntStream.range(0, atrArr.length)
                .map(i -> (atrArr.length - 1 - i))
                .forEach(i -> result[atrArr.length - 1 - i] = Integer.parseInt(atrArr[i]));*/

/*        IntStream.range(0, atrArr.length)
                .boxed()
                .sorted(Collections.reverseOrder())
                .forEach(i -> result[atrArr.length - 1 - i] = Integer.parseInt(atrArr[i]));*/

        IntStream.iterate(atrArr.length - 1, i -> i - 1)
                .limit(atrArr.length)
                .forEach(i -> result[atrArr.length - 1 - i] = Integer.parseInt(atrArr[i]));

        Arrays.stream(Arrays.copyOfRange(result, 0, 3)).forEach(System.out::println);
        System.out.println("Index of first mismatch " + Arrays.mismatch(new int[]{1, 2, 5, 3}, new int[]{1, 2, 5, 6, 9, 10}));
        System.out.println("Lexicographically equals " + Arrays.compare(new int[]{1, 2, 5, 3}, new int[]{1, 2, 5}));
        int[] lookUp = new int[]{1, 2, 5, 3};
        Arrays.sort(lookUp);
        System.out.println("Index of the key is " + Arrays.binarySearch(lookUp, 5));
/*        AtomicInteger counter = new AtomicInteger(atrArr.length);
        IntStream.generate(counter::decrementAndGet)
                .limit(atrArr.length)
                .forEach(i -> result[atrArr.length - 1 - i] = Integer.parseInt(atrArr[i]));*/

        String[][] arr1 = {{"m", "e"}, {"t", "o"}, {"d", "i"}};
        String[][] arr2 = {{"m", "e"}, {"t", "o"}, {"d", "i"}};

        return new StringBuilder().append(n)
                .reverse()
                .chars()
                .map(Character::getNumericValue)
                .toArray();

        //System.out.println("Are equal Arrays: " + Arrays.deepEquals(arr1, arr2));

        //return result;
    }
}

class GrassHopper {
    public static int summation(int n) {

        return (n * (n + 1)) / 2;
    }


}

class Bio {
    public String dnaToRna(String dna) {

        char[] arr = dna.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 'T') {
                arr[i] = 'U';
            }
        }
        return String.valueOf(arr);
/*
          return dna.replaceAll("T","U");
*/

/*        return dna.chars()
                .mapToObj(c -> Character.toString((char)c))
                .map(s -> {
                    if (s.equals("T")) {
                        return "U";
                    } else {
                        return s;
                    }
                }).reduce("", String::concat);*/

/*        StringBuilder sb = new StringBuilder();
        Arrays.stream(dna.split(""))
                .map(c -> {
                    if (String.valueOf(c).equals("T")) {
                        return "U";
                    } else {
                        return String.valueOf(c);
                    }
                })
                .forEach(sb::append);
        return sb.toString();*/
    }
}
