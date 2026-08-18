package com.mastering.lambdas.misc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnagramGroups {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>(Arrays.asList(
                "eat", "tea", "tan", "ate", "nat", "bat"
        ));

        List<List<String>> groupedAnagrams = new AnagramGroups().groupAnagrams(words);
        System.out.println(groupedAnagrams);
    }

    /**
     * i/o: {"eat", "tea", "tan", "ate", "nat", "bat"}; <br>
     * Grouped Anagrams: [[eat, tea, ate], [bat], [tan, nat]]
     *
     * @param words
     * @return
     */
    public List<List<String>> groupAnagrams(List<String> words) {

        return words.stream().collect(Collectors.groupingBy(this::buildClassifier))
                .values()
                .stream()
                .toList();
    }

    public String buildClassifier(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public String buildClassifierV1(String word) {
        String[] arr = word.split("");
        Arrays.sort(arr);
        return String.join("", arr);
    }

    public String buildClassifierV2(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        // Remove whitespace, normalize case
        String cleanWord = word.toLowerCase().replaceAll("\\s", "");

        char[] characters = cleanWord.toCharArray();
        Arrays.sort(characters);
        return new String(characters);
    }

    public String buildClassifierV3(String word) {
        if (word == null || word.isBlank()) {
            return "";
        }
        return word
                .chars()
                .sorted()
                .mapToObj(Character::toString)
                .collect(Collectors.joining(""));
    }

    public String buildClassifierV4(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);

        return Arrays.toString(chars);
    }

    public String buildClassifierV5(String word) {
        StringBuilder result = new StringBuilder(word);
        return result
                .chars()
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(""));
    }

    // If the input is restricted to lowercase English letters
    // Then better than using Arrays.sort() is to use counting sort. Instead of O(k log k), time complexity is O(k)
    // Overall O(N * (k log k)) -> O(N) cause k is constant (assuming word length is not bigger than 100, for example)
    public String buildClassifierV6(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        int[] counts = new int[26];

        for (int i = 0; i < word.length(); i++) {
            counts[word.charAt(i) - 'a']++;
        }

        StringBuilder result = new StringBuilder(word.length());

    /*
        for (int i = 0; i < 26; i++) {
                result.repeat((char) ('a' + i), counts[i]);
        }
    */

        for (int i = 0; i < 26; i++) {
            // result.repeat((char) ('a' + i), counts[i]);
            result.repeat(String.valueOf((char) ('a' + i)), Math.max(0, counts[i]));
        }

        return result.toString();
    }

    // Version 1: Simplified with Streams (Recommended)
    public List<List<String>> groupAnagramsV1(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        return new ArrayList<>(words.stream()
                .collect(Collectors.groupingBy(this::buildClassifier))
                .values());
    }

    // Version 2: Most Efficient (No Stream Overhead)
    public List<List<String>> groupAnagramsV2(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String word : words) {
            String classifier = buildClassifier(word);
            anagramGroups.computeIfAbsent(classifier, k -> new ArrayList<>()).add(word);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    // Version 3: Using the New Classifier for Better Performance
    public List<List<String>> groupAnagramsV3(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String word : words) {
            if (word != null && !word.isEmpty()) {
                String classifier = buildClassifier(word);
                anagramGroups.computeIfAbsent(classifier, k -> new ArrayList<>()).add(word);
            }
        }

        return new ArrayList<>(anagramGroups.values());
    }

    // Version 4: Parallel Processing for Large Lists
    public List<List<String>> groupAnagramsV4(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        // Use parallel stream for large lists (>1000 words)
        boolean isLarge = words.size() > 1000;
        Stream<String> wordStream = isLarge ? words.parallelStream() : words.stream();

        return new ArrayList<>(wordStream
                .filter(word -> word != null && !word.isEmpty())
                .collect(Collectors.groupingByConcurrent(this::buildClassifier))
                .values());
    }

    // Version 5: With Custom Collector (Most Flexible)
    public List<List<String>> groupAnagramsV5(List<String> words) {
        if (words == null || words.isEmpty()) {
            return Collections.emptyList();
        }

        return words.stream()
                .filter(Objects::nonNull)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(this::buildClassifier),
                        map -> new ArrayList<>(map.values())
                ));
    }

    // Version 6: Using Char[] Classifier (Fastest)
    public List<List<String>> groupAnagramsV6(List<String> words) {
        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String word : words) {
            if (word == null || word.isEmpty()) {
                continue;
            }

            String classifier = buildClassifier(word);

            anagramGroups.computeIfAbsent(classifier, k -> new ArrayList<>()).add(word);
        }

        return new ArrayList<>(anagramGroups.values());
    }
}
