package com.mastering.lambdas.chapter2;

import java.util.function.IntUnaryOperator;

public class Factorial {
    IntUnaryOperator fact;

    public Factorial() {
        fact = i -> i == 0 ? 1 : i * fact.applyAsInt(i - 1);
    }
}
