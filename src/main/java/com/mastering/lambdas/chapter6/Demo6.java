package com.mastering.lambdas.chapter6;

import com.mastering.lambdas.chapter3.Book;
import com.mastering.lambdas.chapter3.LibraryInit;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@Setter
public class Demo6 {

    private List<Book> library;

    public Demo6() {
        library = new LibraryInit().getLibrary();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // execute code being benchmarked
        long end = System.currentTimeMillis();
        System.out.println("My task executed in " + (end - start) + " ms.");

        long start2 = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (end2 - start2) + "ms");

        Stream.of(8, 3, 5, 6, 7, 4) // ORDERED, SIZED
                .filter(i -> i % 2 == 0) // ORDERED
                .sorted() // ORDERED, SORTED
                .distinct() // DISTINCT, ORDERED, SORTED
                .map(i -> i + 1) // ORDERED
                .unordered(); // none

        List<String> stringList = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i");
        String joined = stringList
                .parallelStream()
                .map(String::toUpperCase)
                .collect(Collectors.joining());
        System.out.println(joined);

        System.out.println();

        long distinctCount = stringList.parallelStream()
                .unordered()
                .distinct()
                .count();
        System.out.println("Number of distinct strings: " + distinctCount);

        System.out.println();

        //usually produces the output
        //1:1 2:2 3:3 4:4 5:5
        AtomicInteger counter = new AtomicInteger(1);
        IntStream.rangeClosed(1, 5)
                .mapToObj(i -> i + ":" + counter.getAndAdd(1) + " ")
                .forEachOrdered(System.out::print);

        System.out.println();

        //whereas when executed in parallel, you can never predict the ordering of side-effects:
        //produces (for example)
        //1:2 2:1 3:5 4:4 5:3
        AtomicInteger counter2 = new AtomicInteger(1);
        IntStream.rangeClosed(1, 5).parallel()
                .mapToObj(i -> i + ":" + counter2.getAndAdd(1) + " ")
                .forEachOrdered(System.out::print);

        System.out.println();
        System.out.println();

        //One program used a primitive stream:
        OptionalInt max = IntStream.rangeClosed(1, 5)
                .map(i -> i + 1)
                .max();
        max.ifPresent(m -> System.out.println("Max value using primitive int stream is (fast): " + m));

        //and the other a stream of wrapper values:
        Optional<Integer> max2 = Arrays.asList(1, 2, 3, 4, 5).stream()
                .map(i -> i + 1)
                .max(Integer::compareTo);
        max.ifPresent(m -> System.out.println("Max value using a stream of wrapper values is (slow): " + m));
    }
}

