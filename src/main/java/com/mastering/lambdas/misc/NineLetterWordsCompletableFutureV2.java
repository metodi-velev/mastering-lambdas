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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
public class NineLetterWordsCompletableFutureV2 {

    private List<String> oneLetterWordsContainingAorI = new ArrayList<>(Arrays.asList("I", "A"));
    private List<String> words = loadAllWords();
    private List<String> twoLetterWordsContainingAorI;
    private List<String> threeLetterWordsContainingAorI;
    private List<String> fourLetterWordsContainingAorI;
    private List<String> fiveLetterWordsContainingAorI;
    private List<String> sixLetterWordsContainingAorI;
    private List<String> sevenLetterWordsContainingAorI;
    private List<String> eightLetterWordsContainingAorI;
    private List<String> nineLetterWordsContainingAorI;

    private int[] prices = {1, 2, 60, 30, 10, 15, 19, 28, 15};

    public NineLetterWordsCompletableFutureV2() throws Exception {
        CompletableFuture future = CompletableFuture.runAsync(() -> this.twoLetterWordsContainingAorI = loadWordsContainingAorI(2), executor)
                .thenRunAsync((() -> this.threeLetterWordsContainingAorI = loadWordsContainingAorI(3)), executor)
                .thenRunAsync((() -> this.fourLetterWordsContainingAorI = loadWordsContainingAorI(4)), executor)
                .thenRunAsync((() -> this.fiveLetterWordsContainingAorI = loadWordsContainingAorI(5)), executor)
                .thenRunAsync((() -> this.sixLetterWordsContainingAorI = loadWordsContainingAorI(6)), executor)
                .thenRunAsync((() -> this.sevenLetterWordsContainingAorI = loadWordsContainingAorI(7)), executor)
                .thenRunAsync((() -> this.eightLetterWordsContainingAorI = loadWordsContainingAorI(8)), executor)
                .thenRunAsync((() -> this.nineLetterWordsContainingAorI = loadWordsContainingAorI(9)), executor)
                .thenAccept(s -> System.out.println("DONE"));
        future.get();
        /*final List<String> result1;
        final List<String> result2;
        final List<String> result3;
        final List<String> result4;
        final List<String> result5;
        final List<String> result6;
        final List<String> result7;
        final List<String> result8;
        System.out.println("One Letter Words size: " + oneLetterWordsContainingAorI.size());
        System.out.println("------------------");

        System.out.println("Two Letter Words size: " + twoLetterWordsContainingAorI.size());
        result1 = twoLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, oneLetterWordsContainingAorI).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Two Letter Words contained in one letter words after one letter removal: " + result1.size());
        System.out.println("------------------");

        System.out.println("Three Letter Words size: " + threeLetterWordsContainingAorI.size());
        result2 = threeLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result1).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Three Letter Words contained in two letter words after one letter removal: " + result2.size());
        System.out.println("------------------");

        System.out.println("Four Letter Words size: " + fourLetterWordsContainingAorI.size());
        result3 = fourLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result2).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Four Letter Words contained in three letter words after one letter removal: " + result3.size());
        System.out.println("------------------");

        System.out.println("Five Letter Words size: " + fiveLetterWordsContainingAorI.size());
        result4 = fiveLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result3).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Five Letter Words contained in four letter words after one letter removal: " + result4.size());
        System.out.println("------------------");

        System.out.println("Six Letter Words size: " + sixLetterWordsContainingAorI.size());
        result5 = sixLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result4).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Six Letter Words contained in five letter words after one letter removal: " + result5.size());
        System.out.println("------------------");

        System.out.println("Seven Letter Words size: " + sevenLetterWordsContainingAorI.size());
        result6 = sevenLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result5).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Seven Letter Words contained in six letter words after one letter removal: " + result6.size());
        System.out.println("------------------");

        System.out.println("Eight Letter Words size: " + eightLetterWordsContainingAorI.size());
        result7 = eightLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result6).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Eight Letter Words contained in seven letter words after one letter removal: " + result7.size());
        System.out.println("------------------");

        System.out.println("Nine Letter Words size: " + nineLetterWordsContainingAorI.size());
        result8 = nineLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, result7).parallelStream().distinct()).collect(Collectors.toList());
        System.out.println("Nine Letter Words contained in eight letter words after one letter removal: " + result8.size());

        Instant start = Instant.now();
        CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> nineLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, eightLetterWordsContainingAorI).parallelStream().distinct()).collect(Collectors.toList()), executor);
        future.join();
        System.out.println("Nine letter words in eight letter words after one letter removal: " + future.get().size());

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Runtime is: " + timeElapsed.getSeconds());*/
    }

    private final Executor executor = Executors.newFixedThreadPool(7, (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

//    private final ForkJoinPool executor = new ForkJoinPool(8);

    public static CompletableFuture<Integer> create(int n) {
        return CompletableFuture.supplyAsync(() -> n);
    }

    public static void main(String[] args) throws Exception {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> beautiful = CompletableFuture.supplyAsync(() -> "Beautiful ");
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> "World ");
        CompletableFuture<String> to = CompletableFuture.supplyAsync(() -> "To ");
        CompletableFuture<String> matthias = CompletableFuture.supplyAsync(() -> "Matthias ");
        CompletableFuture<String> wolf = CompletableFuture.supplyAsync(() -> "Wolf ");
        CompletableFuture<String> completableFuture
                = hello
                .thenCombine(beautiful, (s1, s2) -> s1 + s2)
                .thenCombine(world, (s1, s2) -> s1 + s2)
                .thenCombine(to, (s1, s2) -> s1 + s2)
                .thenCombine(matthias, (s1, s2) -> s1 + s2)
                .thenCombine(wolf, (s1, s2) -> s1 + s2);

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(hello, beautiful, world);

        hello.completeExceptionally(
                new RuntimeException("Calculation failed!"));
        System.out.println("COMBINED FUTURE: " + combinedFuture.get());

        System.out.println(hello.isDone());
        System.out.println(beautiful.isDone());
        System.out.println(world.isDone());

        String combined = Stream.of(hello, beautiful, world)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));

        System.out.println("Hello Beautiful World COMBINED: " + combined);

        System.out.println("Concatenation Result is: " + completableFuture.get());

        System.out.println("Future one done? " + completableFuture.isDone());
/*        NineLetterWordsCompletableFuture test = new NineLetterWordsCompletableFuture();
        final int max = Arrays.stream(test.prices).max().orElseThrow(IllegalArgumentException::new);
        System.out.println(max);*/
//        create(2).thenCompose(data -> create(data)).thenAccept(System.out::println);
        NineLetterWordsCompletableFutureV2 test = new NineLetterWordsCompletableFutureV2();
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
                .thenApply(data -> twoLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> threeLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> fourLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> fiveLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> sixLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> sevenLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> eightLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenApply(data -> nineLetterWordsContainingAorI.parallelStream().flatMap(w -> addValidWords(w, data).parallelStream().distinct()).collect(Collectors.toList()))
                .thenAcceptAsync(result::addAll, executor);
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

    private List<String> loadWordsContainingAorI(int numLetters) {
        return words.parallelStream().filter(w -> w.length() == numLetters).collect(Collectors.toList());
    }

    private List<String> loadAllWords() throws Exception {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            List<String> ret = br.lines().skip(2).filter(w -> w.length() <= 9).filter(w -> (w.contains("I") || w.contains("A"))).collect(Collectors.toList());
            return ret;
        }
    }

}

class VenkatCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int result = 0;
/*        CompletableFuture.supplyAsync(VenkatCompletableFuture::generate)
                .exceptionally(VenkatCompletableFuture::processError)
                .thenApply(data -> data * 2)
                .exceptionally(VenkatCompletableFuture::processError)
                .thenAccept(VenkatCompletableFuture::printIt);*/
        CompletableFuture<Integer> goog =
                CompletableFuture.supplyAsync(() -> getStockPrice("GOOG", 1));
        CompletableFuture<Integer> amzn =
                CompletableFuture.supplyAsync(() -> getStockPrice("AMZN", 1));
        CompletableFuture<Integer> facebook =
                CompletableFuture.supplyAsync(() -> getStockPrice("FCBOOK", 3));

        goog.thenAccept(value -> System.out.println(value));
        amzn.thenAccept(value -> System.out.println(value));
        facebook.thenAccept(value -> System.out.println(value));

//        goog.thenCombine(amzn, Integer::sum).thenCombine(facebook, Integer::sum).thenAccept(System.out::println);
        CompletableFuture<Integer> combinedSum = goog.thenCombine(amzn, Integer::sum).thenCombine(facebook, Integer::sum);

        System.out.println("Combined sum done? " + combinedSum.isDone());
        System.out.println("Combined sum result: " + combinedSum.get());


    }

    private static int getStockPrice(String ticker, int numberOfShare) {
        int price = 1000;
        if (ticker.equals("GOOG"))
            price = 500;
        return numberOfShare * price;
    }

    private static Integer processError(Throwable th) {
        System.out.println("ERROR: " + th.getMessage());
        throw new RuntimeException("I hate to tell you");
//        return 1;
    }

    private static void printIt(Integer value) {
//        System.out.println(value + " " + Thread.currentThread());
        System.out.println(value);
    }

    private static int generate() {
//        System.out.println("doing work " + Thread.currentThread());
        throw new RuntimeException("oops something went wrong");
//        return 2;
    }
}

class VenkatThenCompose {
    private static CompletableFuture<Integer> getStockPrice(String ticker, int numberOfShare) {
        return CompletableFuture.supplyAsync(() -> {
            int price = 1000;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (ticker.equals("GOOG"))
                price = 500;
            return numberOfShare * price;
        });
    }

    public static void main(String[] args) throws Exception {
        List<String> symbols = Arrays.asList("GOOG", "AMZN");
        int res = CompletableFuture.supplyAsync(() -> symbols.get(0))
                .thenCompose(symbol -> getStockPrice(symbol, 3))
                .get();
        System.out.println(res);
        System.out.println("----------------------------------");
        CompletableFuture<Integer> finalResult = compute().thenCompose(VenkatThenCompose::computeAnother);
        System.out.println(finalResult.get());
    }

    private static CompletableFuture<Integer> compute() {
        return CompletableFuture.supplyAsync(() -> 10);
    }

    private static CompletableFuture<Integer> computeAnother(Integer i) {
        return CompletableFuture.supplyAsync(() -> 10 + i);
    }
}

class VenkatParallelVsCompletableFuturePart3 {
    static int MAX = 1500;

    //imperative style - the structure of sequential code is very different from the structure of concurrent code
    //using streams - the structure of sequential code is identical to the structure of concurrent code
    //mutability and concurrent code do not go well together
    public static void main(String[] args) {
        // double resultDouble = IntStream.range(0, MAX).mapToDouble(VenkatParallelVsCompletableFuturePart3::compute).sum();
        //System.out.println(resultDouble);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

/*        List<Thread> workers = Stream
                .generate(() -> new Thread(() -> streamNumbers(numbers.parallelStream())))
                        .limit(100)
                        .collect(toList());

        workers.forEach(Thread::start);*/

/*        int total = 0;
        for (int e : numbers)
            if (e % 2 == 0)
                total += e * 2;
        System.out.println(total);

        //imperative style has accidental complexity
        //functional style has less complexity and is als easier to parallelize

        //functional style - functional composition
        Integer result = numbers.stream()
                .filter(e -> e % 2 == 0)
                .mapToInt(e -> e * 2)
                .reduce(0, (e1, e2) -> e1 + e2);
        System.out.println(result);

        use(numbers.stream());*/
        streamNumbers(numbers.stream());
/*        System.out.println(numbers.parallelStream().reduce(0, (totalSum, e) -> add(totalSum, e)) + 30);
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println(ForkJoinPool.commonPool());*/
    }

    private static void streamNumbers(Stream<Integer> numbers) {
        numbers.parallel()
                .map(VenkatParallelVsCompletableFuturePart3::transform)
                .forEach(e -> {});
    }


    public static double compute(double number) {
        double result = 0;
        for (int i = 0; i < MAX; i++)
            for (int j = 0; j < MAX; j++)
                result += Math.sqrt(i * j);
        return result;
    }

    private static int add(int a, int b) {
        int result = a + b;
        System.out.println("a= " + a + " b= " + b + " result" + " result " + result + "--" + Thread.currentThread());
        return result;
    }

    private static void use(Stream<Integer> stream) {
        streamNumbers(stream
                .parallel());
    }

    private static int transform(int number) {
        System.out.println("t: " + number + "--" + Thread.currentThread());
        sleep(1000);
        return number * 1;
    }

    public static boolean sleep(int ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
