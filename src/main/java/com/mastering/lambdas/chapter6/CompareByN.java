package com.mastering.lambdas.chapter6;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark) // The objects of this scope (e.g., instance
// variables) are shared between all threads.
public class CompareByN {
    // entire benchmark is run for each value of the @Param annotation
    @Param({"1", "10", "100", "1000", "10000", "100000", "1000000"})
    public int N;
    private final int payload = 50;
    private List<Integer> integerList;

    @Setup(Level.Trial)
    public void setUp() {
        integerList = IntStream.range(0, N).boxed().collect(Collectors.toList());
    }

    @Benchmark
    public void iterative(Blackhole bh) {
        for (Integer i : integerList) {
            Blackhole.consumeCPU(payload);
            bh.consume(i);
        }
    }

    @Benchmark
    public Optional<Integer> sequential() {
        return integerList.stream()
                .filter(l -> {
                    Blackhole.consumeCPU(payload);
                    return false;
                })
                .findFirst();
    }

    @Benchmark
    public Optional<Integer> parallel() {
        return integerList.stream().parallel()
                .filter(l -> {
                    Blackhole.consumeCPU(payload);
                    return false;
                })
                .findFirst();
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
