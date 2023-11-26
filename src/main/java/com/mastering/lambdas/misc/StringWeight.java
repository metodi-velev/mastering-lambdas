package com.mastering.lambdas.misc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class StringWeight {
    Map<String, BigDecimal> mappings = new HashMap<>();
    Map<String, Long> mappingsLong = new HashMap<>();
    Map<String, Long> mappingsLongMaxBy = new HashMap<>();
    StringBuilder resultString = new StringBuilder();
    StringBuilder resultStringLong = new StringBuilder();
    StringBuilder resultStringLongMaxBy = new StringBuilder();

    /*
     * Complete the 'smallestString' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts LONG_INTEGER weight as parameter.
     * A = 1
     * B = 2 * A + 1 = 3
     * C = 3 * B + 3 = 12
     * D = 4 * C + 4 = 52
     * E = 5 * D + 5 = 265
     * F = 6 * E + 6 = 1596 -> Shortest is F. But 265*6 + 3 + 3 = 1596 resulting to EEEEEEBB
     * G = 7 * F + 7 = 11179
     * H = 8 * G + 8 = 89440
     * .
     * Z = ...
     *
     * Return the shortest String matching which value matches the weight
     * Example: weight=64 output: DC
     * Example: weight=10000 output:  FFFFFFEDDDB
     * Example: weight=15000 output:  GFFEEDCCCBBBAA
     * Example: weight=86000 output:  GGGGGGGFFFFEEEEECCCAA
     * Example: weight=105000 output: HGFFEEEEDDCCA
     */

    public StringWeight() {
        initializeMap();
        initializeMapLong();
        initializeMapLongMaxBy();
    }

    private void initializeMap() {

        mappings.put("A", BigDecimal.valueOf(1L));
        mappings.put("B", BigDecimal.valueOf(3L));

        BigDecimal result = new BigDecimal(3);
        char userLetter = 'C';
        for (int count = 3; count <= 26; count++) {
            result = (result.multiply(BigDecimal.valueOf(count))).add(BigDecimal.valueOf(count));
            System.out.print((char) (userLetter + count));
            mappings.put(String.valueOf((char) (userLetter + count - 3)), result);
        }
        System.out.println();
        mappings.forEach((k, v) -> System.out.println(k + "=" + v));

    }

    private void initializeMapLong() {

        mappingsLong.put("A", 1L);
        mappingsLong.put("B", 3L);

        long result = 3L;
        char userLetter = 'C';
        for (int count = 3; count <= 26; count++) {
            result = result * count + count;
            System.out.print((char) (userLetter + count));
            mappingsLong.put(String.valueOf((char) (userLetter + count - 3)), result);
        }
        System.out.println();
        mappingsLong.forEach((k, v) -> System.out.println(k + "=" + v));

    }

    private void initializeMapLongMaxBy() {

        mappingsLongMaxBy.put("A", 1L);
        mappingsLongMaxBy.put("B", 3L);

        long result = 3L;
        char userLetter = 'C';
        for (int count = 3; count <= 26; count++) {
            result = result * count + count;
            System.out.print((char) (userLetter + count));
            mappingsLongMaxBy.put(String.valueOf((char) (userLetter + count - 3)), result);
        }
        System.out.println();
        mappingsLongMaxBy.forEach((k, v) -> System.out.println(k + "=" + v));

    }

    @SuppressWarnings("ReassignedVariable")
    public String smallestString(long weight) {
        Map<BigDecimal, String> mapReverted = mappings.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey,
                        (o, n) -> o.length() < n.length() ? o : n,
                        TreeMap::new
                ));

        if (weight == 0L) {
            return resultString.toString();
        }
        BigDecimal weightBigDecimal = new BigDecimal(weight);
        BigDecimal minResult = new BigDecimal(0);
        BigDecimal remainder = new BigDecimal(0);
        // Write your code here
        for (Map.Entry<String, BigDecimal> entry : mappings.entrySet()) {
            String key = entry.getKey();
            BigDecimal value = entry.getValue();
            BigDecimal[] resultAndRemainder = value.divideAndRemainder(weightBigDecimal);
            BigDecimal result = resultAndRemainder[0];
            remainder = resultAndRemainder[1];
            if (result.compareTo(new BigDecimal(1)) == 0 && remainder.equals(new BigDecimal(0))) {
                resultString.append(key).toString();
                //return smallestString(weight - (mappings.get(key).longValue() * result.longValue()));
                return resultString.toString();
            }
            if (result.compareTo(new BigDecimal(1)) >= 0) {
                String newKey = "";
                newKey = String.valueOf((char) (key.toCharArray()[0] - 1));
                BigDecimal[] intermediate = weightBigDecimal.divideAndRemainder(mappings.get(newKey));
                for (long i = 1; i <= intermediate[0].longValue(); i++) {
                    resultString.append(newKey).toString();
                }
                return smallestString(weight - (mappings.get(newKey).longValue() * intermediate[0].longValue()));
            }

            minResult = minResult.compareTo(result) > 0 ? minResult : result;
        }

        return resultString.toString();

        //        List<BigDecimal> result = mappings.entrySet().stream()
//                .flatMap(e -> mappings.entrySet().stream()
//                        .map(e2 -> e.getValue().add(e2.getValue()))
//                )
//                .filter(e -> e.equals(BigDecimal.valueOf(weight)))
//                .collect(Collectors.toList());
    }

    public String smallestStringLong(long weight) {

        if (weight == 0L) {
            return resultStringLong.toString();
        }
        long remainder = 0L;
        // Write your code here
        for (Map.Entry<String, Long> entry : mappingsLong.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            long result = value / weight;
            remainder = value - result * weight;
            if (result == 1 && remainder == 0) {
                resultStringLong.append(key).toString();
                //return smallestStringLong(weight - (mappingsLong.get(key) * result));
                return resultStringLong.toString();
            }
            if (result >= 1) {
                String newKey = "";
                newKey = String.valueOf((char) (key.toCharArray()[0] - 1));
                long intermediate = weight / mappingsLong.get(newKey);
                for (long i = 1; i <= intermediate; i++) {
                    resultStringLong.append(newKey).toString();
                }
                return smallestStringLong(weight - (mappings.get(newKey).longValue() * intermediate));
            }
        }

        return resultStringLong.toString();
    }

    public Optional<Map.Entry<String, Long>> getMaxLessThanWeight(long weight) {
        return mappingsLongMaxBy.entrySet().stream()
                .filter(e -> e.getValue() <= weight)
                .max(Map.Entry.comparingByValue());
    }

    public String smallestStringMaxBy(long weight) {
        if (weight == 0L) {
            return resultStringLongMaxBy.toString();
        }

        Optional<Map.Entry<String, Long>> result = getMaxLessThanWeight(weight);

        if (result.isPresent()) {
            Long divider = result.get().getValue();
            long wholeNum = weight / divider;
            long remainder = weight - (wholeNum * divider);
//            if (wholeNum == 1 && remainder == 0) {
//                resultStringLongMaxBy.append(result.get().getKey());
//                return resultStringLongMaxBy.toString();
//            }
            for (int i = 1; i <= wholeNum; i++) {
                resultStringLongMaxBy.append(result.get().getKey());
            }
            return smallestStringMaxBy(remainder);
        }

        System.out.println("Entry MaxBy is: " + Objects.requireNonNull(result).get().getKey() + " : " + Objects.requireNonNull(result).get().getValue());

        return resultStringLongMaxBy.toString();
    }

}

class Solution3 {
    public static void main(String[] args) throws IOException {
       /* BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        long weight = Long.parseLong(bufferedReader.readLine().trim()); */
        StringWeight obj = new StringWeight();

        String result = obj.smallestString(86000L);

        System.out.println();

        System.out.println("Result BigDecimal is: " + result);

        System.out.println("--------------------------------");

        String resultLong = obj.smallestStringLong(86000L);
        System.out.println("Result Long is: " + resultLong);

        System.out.println();

        System.out.println(String.valueOf((char) ("F".toCharArray()[0] - 1)));

        BigDecimal divisor = new BigDecimal(34);
        BigDecimal divider = new BigDecimal(6);
        BigDecimal[] resultDiv = divisor.divideAndRemainder(divider);
        System.out.println("Result is: " + resultDiv[0] + " and Remainder is: " + resultDiv[1]);
        /*bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close(); */
        System.out.println("Max By is: " + obj.smallestStringMaxBy(86000L));
    }
}
