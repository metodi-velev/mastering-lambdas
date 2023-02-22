package com.mastering.lambdas.chapter1;

import java.io.IOException;

@FunctionalInterface
interface IOFunction<T, R> {
    R apply(T t) throws IOException;
}
