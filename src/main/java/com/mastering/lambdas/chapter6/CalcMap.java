package com.mastering.lambdas.chapter6;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalcMap {
    static Map<String, String> calcMap() {
        Map<String, String> map = Stream.of(new String[][]{
                {"alpha", "X"},
                {"bravo", "Y"},
                {"charlie", "Z"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        String str = "alpha-bravo-charlie";
        map.replaceAll(str::replace);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(calcMap());
    }
}
