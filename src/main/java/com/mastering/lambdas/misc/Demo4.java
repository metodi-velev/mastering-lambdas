package com.mastering.lambdas.misc;

import lombok.Getter;

@Getter
public class Demo4 {

    public static String printMsg() {
        return "Test is";
    }
    public Demo4() {

    }

    public static void main(String[] args) {
        Demo4 obj = null;
        System.out.println(obj.printMsg());
    }
}
