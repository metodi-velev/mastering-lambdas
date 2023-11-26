package com.mastering.lambdas.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
public class NineLetterWords {

    private List<String> words;
    private List<String> nineLetterWordsContainingAorI;
    private List<String> twoLetterWordsContainingAorI;
    private List<String> threeLetterWordsContainingAorI;
    private List<String> fourLetterWordsContainingAorI;
    private List<String> fiveLetterWordsContainingAorI;
    private List<String> sixLetterWordsContainingAorI;
    private List<String> sevenLetterWordsContainingAorI;
    private List<String> eightLetterWordsContainingAorI;

    private Function<String, List<String>> function = w -> addValidWords(w, twoLetterWordsContainingAorI);

    public NineLetterWords() throws Exception {
        this.words = loadAllWords();
        this.twoLetterWordsContainingAorI = loadWordsContainingAorI(2);
        this.threeLetterWordsContainingAorI = loadWordsContainingAorI(3);
        this.fourLetterWordsContainingAorI = loadWordsContainingAorI(4);
        this.fiveLetterWordsContainingAorI = loadWordsContainingAorI(5);
        this.sixLetterWordsContainingAorI = loadWordsContainingAorI(6);
        this.sevenLetterWordsContainingAorI = loadWordsContainingAorI(7);
        this.eightLetterWordsContainingAorI = loadWordsContainingAorI(8);
        this.nineLetterWordsContainingAorI = loadWordsContainingAorI(9);
    }

    public static void main(String[] args) throws Exception {
        NineLetterWords test = new NineLetterWords();
        long start = System.currentTimeMillis();

        List<String> resultList = test.getRelevantNineLetterWords();

        long end = System.currentTimeMillis();
        System.out.println("Runtime is: " + ((end - start) / 1000));

        System.out.println("List SIZE is: " + resultList.size());
        resultList.forEach(System.out::println);
    }

    private List<String> getRelevantNineLetterWords() {
        List<String> resultList3 = threeLetterWordsContainingAorI.parallelStream().flatMap(w -> function.apply(w).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList4 = fourLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList3).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList5 = fiveLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList4).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList6 = sixLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList5).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList7 = sevenLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList6).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList8 = eightLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList7).parallelStream().distinct()).collect(Collectors.toList());
        List<String> resultList9 = nineLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, resultList8).parallelStream().distinct().sorted()).collect(Collectors.toList());
        return resultList9;
    }

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

    private List<String> loadWordsContainingAorI(int numLetters) {
        return words.parallelStream().filter(w -> w.length() == numLetters && (w.contains("I") || w.contains("A"))).collect(Collectors.toList());
    }

    private List<String> loadAllWords() throws Exception {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            List<String> ret = br.lines().skip(2).collect(Collectors.toList());
            return ret;
        }
    }

}
