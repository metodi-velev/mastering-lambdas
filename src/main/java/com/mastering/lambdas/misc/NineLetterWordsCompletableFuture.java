package com.mastering.lambdas.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * We are using an iterative bottom-up approach. In order to reduce the amount of relevant words and the time complexity of the algorithm,
 * first we create dictionaries containing all two, three until nine letters long words containing "A" or "I". Then we check iteratively
 * whether the words in a dictionary are contained in the "neighbour" dictionary by removing a letter each time. We start from the bottom up -
 * first we compare the two-letter dictionary against matches in the one-word dictionary comprising the letters "A" and "I". Then we compare
 * the three-letter words dictionary against the result from the previous comparison until we finally get to the last dictionary with nine-letter words.
 * This way we save a lot of computations since the set of the relevant two- and three-letter words dictionaries are much smaller in size compared to
 * the nine- and eight-letter ones. We additionally avoid extra computations by basing our search each time on the previous result set.
 * <br>
 * <br>
 * <br>
 */
public class NineLetterWordsCompletableFuture {

    private List<String> words;
    private List<String> oneLetterWordsContainingAorI = new ArrayList<>(Arrays.asList("I", "A"));
    private int[] prices = {1, 2, 60, 30, 10, 15, 19, 28, 15};

    public NineLetterWordsCompletableFuture() throws Exception {
        this.words = loadAllWords();
    }

    public static CompletableFuture<Integer> create(int n) {
        return CompletableFuture.supplyAsync(() -> n);
    }

    public static void main(String[] args) throws Exception {
/*        NineLetterWordsCompletableFuture test = new NineLetterWordsCompletableFuture();
        final int max = Arrays.stream(test.prices).max().orElseThrow(IllegalArgumentException::new);
        System.out.println(max);*/
//        create(2).thenCompose(data -> create(data)).thenAccept(System.out::println);
        NineLetterWordsCompletableFuture test = new NineLetterWordsCompletableFuture();
        Instant start = Instant.now();

        CompletableFuture<List<String>> future = new CompletableFuture<>();
        List<String> resultList = test.getRelevantNineLetterWords(future);
        future.complete(test.oneLetterWordsContainingAorI);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Runtime is: " + timeElapsed.getSeconds());

        System.out.println("List SIZE is: " + resultList.size());
        resultList.forEach(System.out::println);
    }

    private List<String> getRelevantNineLetterWords(CompletableFuture<List<String>> future) {
        List<String> result = new ArrayList<>();
        future
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 2).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 3).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 4).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 5).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 6).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 7).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 8).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> words.parallelStream().filter(w -> w.length() == 9).flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenAccept(result::addAll);
        return result;
    }

/*    private List<String> addValidWords(String word, List<String> wordList) {
        List<String> result = new ArrayList<>();
        char[] charArray = word.toCharArray();
        wordList.parallelStream().forEach(w -> {
            for (int i = 0; i < charArray.length; i++) {
                char[] arr1 = Arrays.copyOfRange(charArray, 0, i);
                char[] arr2 = Arrays.copyOfRange(charArray, i + 1, charArray.length);
                if (w.equals(String.copyValueOf(arr1).concat(String.copyValueOf(arr2))))
                    result.add(word);
            }
        });
        return result;
    }*/

    private List<String> addValidWords(String word, List<String> wordList) {
        List<String> result = new ArrayList<>();
        IntStream.range(0, word.length()).parallel().forEach(i -> {
            StringBuilder sb = new StringBuilder(word);
            sb.deleteCharAt(i);
            if (wordList.contains(sb.toString())) {
                result.add(word);
            }
        });
        return result;
    }

    private List<String> loadAllWords() throws Exception {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            List<String> ret = br.lines().skip(2).filter(w -> w.length() <= 9).filter(w -> (w.contains("I") || w.contains("A"))).collect(Collectors.toList());
            return ret;
        }
    }

}
