package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;

import java.util.Arrays;

public class EgExample {

    @Eg(given = {"2", "1"}, returns = "3")
    @Eg(given = {"-1", "Integer.MIN_VALUE"}, returns = "Integer.MAX_VALUE")
    public static int add(int a, int b) {
        return a + b;
    }

    @Eg(given = {"\"World\""}, returns = "\"Hello, World!\"")
    public static String greet(String target) {
        return "Hello, "+target+"!";
    }

    @Eg(given = {"1.0", "2.0"}, returns = "0.5")
    @Eg(given = {"1.0", "3.0"}, returns = "0.33333", delta = 0.001)
    @Eg(given = {"1.0", "0.0"}, returns = "Double.POSITIVE_INFINITY")
    @Eg(given = {"0.0", "0.0"}, returns = "Double.NaN")
    public static double divide(double numerator, double divisor) {
        return numerator / divisor;
    }

    @Eg(given = {"5.0"}, returns = "5.0")
    @Eg(given = {"2.0", "3.0", "5.0"}, returns = "30.0")
    public static double multiply(double a, double... others) {
        return Arrays.stream(others).reduce(a, (b,c) -> b*c);
    }

    @Eg(given = {"7f", "5f"}, returns = "35f")
    @Eg(given = {"null", "5f"}, returns = "null")
    @Eg(given = {"1f", "(Float) null"}, returns = "null")
    public static Float multiply(Float a, Float b) {
        return (a==null || b==null)  ?  null  :  a * b;
    }


    @Eg(given = {"null", "5.0"}, returns = "null")
    @Eg(given = {"1.0", "(Double) null"}, returns = "null")
    public static Double multiply(Double a, Double b) {
        return (a==null || b==null)  ?  null  :  a * b;
    }


    @Eg(given = {"5"}, returns = "5")
    @Eg(given = {"2", "3", "5"}, returns = "30")
    public static int multiply(int a, int... others) {
        return Arrays.stream(others).reduce(a, (b,c) -> b*c);
    }

    // When testing a non-static method, the public default constructor is used
    private final int decrementStep;

    public EgExample() {
        this(1);
    }

    public EgExample(int decrementStep) {
        this.decrementStep = decrementStep;
    }

    @Eg(given = {"Integer.MIN_VALUE"}, returns = "Integer.MAX_VALUE")
    @Eg(given = {"1"}, returns = "0")
    public int decrement(int a) {
        return a - decrementStep;
    }
}
