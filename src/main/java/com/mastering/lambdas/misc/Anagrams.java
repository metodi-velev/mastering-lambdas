package com.mastering.lambdas.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class Anagrams {

    // Adapter from Stream<E> to Iterable<E>
    public static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    // Adapter from Iterable<E> to Stream<E>
    public static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static void main(String[] args) throws Exception {
        List<String> words1 = Arrays.asList("Matthias", "Wolf", "Mitko", "Wolf", "Snezka", "Wolf", "Alex", "Dimitrov", "Plamen", "Dimitrov");
        Map<String, Long> freq = words1.stream()
                .collect(groupingBy(String::toLowerCase, counting()));
        freq.entrySet().stream().sorted(comparing(Map.Entry::getKey)).forEach((e) -> System.out.println("Key: " + e.getKey() + " appears " + e.getValue() + " times."));

        // Pipeline to get a top-ten list of words from a frequency table
        List<String> topTwo = freq.keySet().stream()
                .sorted(comparing(freq::get).reversed())
                .limit(2)
                .collect(toList());
        topTwo.stream().forEach(System.out::println);

        Supplier<Instant> s = Instant::now;
        Map<String, Integer> stringLength = new HashMap<>();
        stringLength.put("John", 5);
        stringLength.computeIfPresent("John", (k, v) -> k.length());
        System.out.println("HashMap: " + stringLength);
        System.out.println("Are Anagrams? " + areAnagrams("Riba", "bira"));
        System.out.println("----------------------------------------------");
        List<String> words = loadAllWords();
        Map<String, List<String>> map = words.parallelStream().collect(groupingBy(word -> alphabetize(word)));
        map.values().parallelStream()
                .filter(group -> group.size() >= 1)
                .sorted(Comparator.comparingInt(List::size))
                .forEachOrdered(g -> System.out.println(g.size() + ": " + g));
    }

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }

    private static Boolean areAnagrams(String s1, String s2) {
        char[] a1 = s1.toLowerCase().toCharArray();
        Arrays.sort(a1);
        char[] a2 = s2.toLowerCase().toCharArray();
        Arrays.sort(a2);
        return Arrays.equals(a1, a2);
    }

    private static List<String> loadAllWords() throws Exception {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/EchNet/anagram/master/words/src/dictionary.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            List<String> ret = br.lines().skip(2).filter(w -> w.length() <= 9).filter(w -> (w.contains("I") || w.contains("A"))).collect(toList());
            return ret;
        }
    }
}

//The power set of {a, b, c} is {{}, {a}, {b}, {c}, {a, b}, {a, c}, {b, c}, {a, b, c}}.
// Returns the power set of an input set as custom collection
class PowerSet {
    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s);
        if (src.size() > 30)
            throw new IllegalArgumentException("Set too big " + s);
        return new AbstractList<Set<E>>() {
            @Override
            public int size() {
                return 1 << src.size(); // 2 to the power srcSize
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set) o);
            }

            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();
                for (int i = 0; index != 0; i++, index >>= 1)
                    if ((index & 1) == 1)
                        result.add(src.get(i));
                return result;
            }
        };
    }
}

// Returns a stream of all the sublists of its input list
class SubLists {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        of(list).forEach(System.out::println);
    }
 /*   private static <E> Stream<List<E>> of(List<E> list) {
        return Stream.concat(Stream.of(Collections.emptyList()),
                prefixes(list).flatMap(SubLists::suffixes));
    }*/

    // Returns a stream of all the sublists of its input list
    private static <E> Stream<List<E>> of(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start ->
                        IntStream.rangeClosed(start + 1, list.size())
                                .mapToObj(end -> list.subList(start, end)))
                .flatMap(x -> x);
    }

    private static <E> Stream<List<E>> prefixes(List<E> list) {
        return IntStream.rangeClosed(1, list.size())
                .mapToObj(end -> list.subList(0, end));
    }

    private static <E> Stream<List<E>> suffixes(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start -> list.subList(start, list.size()));
    }
}

class PrimeCounting {

    public static void main(String[] args) {
        int[] numbers = getRandoms(6, 1, 50);
        IntStream.rangeClosed(1, numbers.length).forEach(i -> {
            String suffix;
            switch (i) {
                case 1:
                    suffix = "-вото";
                    break;
                case 2:
                    suffix = "-рото";
                    break;
                default:
                    suffix = "-тото";
                    break;
            }
            System.out.println(i + suffix + " число е: " + numbers[i - 1]);
        });

        Instant start = Instant.now();

        long number = 10000000;

        IntStream.range(0, 3).forEach(w -> pi(number));

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Runtime is: " + timeElapsed.getSeconds());

    }

    private static int[] getRandoms(int size, int startInclusive, int endExclusive) {
        SplittableRandom random = new SplittableRandom();
        IntStream stream = random.ints(size, startInclusive, endExclusive);
        return stream.distinct().toArray();
    }

    // Prime-counting stream pipeline - benefits from parallelization
    private static long pi(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
}

class StopThread {
    private static boolean stopRequested = false;

    private static synchronized void requestStop() {
        stopRequested = true;
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested()) {
                i++;
                System.out.println(i);
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}

// Broken! - How long would you expect this program to run?
class StopThreadBroken {
    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested) {
                i++;
                System.out.println(i);
            }
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}

class CountDownLatchExample2 implements Runnable {
    private List<String> outputScraper;
    private CountDownLatch countDownLatch;

    public CountDownLatchExample2(List<String> outputScraper, CountDownLatch countDownLatch) {
        this.outputScraper = outputScraper;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        doSomeWork();
        outputScraper.add("Counted down");
        countDownLatch.countDown();
    }

    private void doSomeWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Do some work");
    }
}

// Broken - invokes alien method from synchronized block!
class ObservableSet<E> extends ForwardingSet<E> {
    public ObservableSet(Set<E> set) {super(set);}

    private final List<SetObserver<E>> observers
            = new ArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }

    private void notifyElementAdded(E element) {
        //Bad - causes deadlock
/*        synchronized (observers) {
            for (SetObserver<E> observer : observers)
                observer.added(this, element);
        }*/
        List<SetObserver<E>> snapshot = null;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        // Good - solves deadlock and exceptions. Alien method moved outside of synchronized block - open calls
        for (SetObserver<E> observer : snapshot)
            observer.added(this, element);
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added)
            notifyElementAdded(element);
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c)
            result |= add(element); // Calls notifyElementAdded
        return result;
    }

    public static void main(String[] args) {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
        //set.addObserver((s, e) -> System.out.println(e));

        // Causes ConcurrentModificationException
/*        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23)
                    s.removeObserver(this);
            }
        });*/
        // Observer that uses a background thread needlessly
        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec =
                            Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> s.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}

@FunctionalInterface
interface SetObserver<E> {
    // Invoked when an element is added to the observable set
    void added(ObservableSet<E> set, E element);
}

// Reusable forwarding class
class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;

    public ForwardingSet(Set<E> s) {this.s = s;}

    public void clear() {s.clear();}

    public boolean contains(Object o) {return s.contains(o);}

    public boolean isEmpty() {return s.isEmpty();}

    public int size() {return s.size();}

    public Iterator<E> iterator() {return s.iterator();}

    public boolean add(E e) {return s.add(e);}

    public boolean remove(Object o) {return s.remove(o);}

    public boolean containsAll(Collection<?> c) {return s.containsAll(c);}

    public boolean addAll(Collection<? extends E> c) {return s.addAll(c);}

    public boolean removeAll(Collection<?> c) {return s.removeAll(c);}

    public boolean retainAll(Collection<?> c) {return s.retainAll(c);}

    public Object[] toArray() {return s.toArray();}

    public <T> T[] toArray(T[] a) {return s.toArray(a);}

    @Override
    public boolean equals(Object o) {return s.equals(o);}

    @Override
    public int hashCode() {return s.hashCode();}

    @Override
    public String toString() {return s.toString();}
}