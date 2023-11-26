package com.mastering.lambdas.misc.soap.calculator;

public class CalculatorClient {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        CalculatorSoap calculatorSoap = calculator.getCalculatorSoap();
        System.out.println("Result of Multiplication is: " + calculatorSoap.multiply(33, 3));
        System.out.println("Result of Division is: " + calculatorSoap.divide(99, 33));
    }
}
