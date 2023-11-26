package com.mastering.lambdas.misc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFrequency {

    public static final String WORDS_TXT = "src/main/java/com/mastering/lambdas/misc/words.txt";

    private String readFileUsingBufferedReader() {
//        StringBuilder sb = new StringBuilder();
        String text = "";
        try (BufferedReader br = new BufferedReader(new FileReader(WORDS_TXT))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line).append(" ");
//                text = sb.toString();
//            }
            text = br.lines().collect(Collectors.joining(" "));  // Since Java 8
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    private String readFileUsingScanner() {
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(WORDS_TXT))) {
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next()).append(" ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    private String readFileUsingNIO() {
        List<String> read;
        try {
            read = Files.readAllLines(Paths.get(WORDS_TXT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.join(" ", read);
    }

    private String readFileUsingJava8FilesLines() {
        String result;
        try (Stream<String> lines = Files.lines(Paths.get(WORDS_TXT))) {
            result = lines.collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Map<String, Long> calculateFrequencyMap(String text) {
        String[] words = text.split("\\s+");
        return Arrays.stream(words).
                collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> newValue, LinkedHashMap::new));
    }

    public static void main(String[] args) {
        WordFrequency wordFrequency = new WordFrequency();

        String text = wordFrequency.readFileUsingScanner();

        wordFrequency.calculateFrequencyMap(text).forEach((k, v) -> System.out.println("Word " + k + " appears " + v + " times"));
    }
}
