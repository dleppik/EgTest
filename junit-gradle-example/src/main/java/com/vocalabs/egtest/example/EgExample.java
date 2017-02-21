package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;

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


    // When testing a non-static method, the default constructor is used
    private final int decrementStep;

    public EgExample() {
        this(1);
    }

    public EgExample(int decrementStep) {
        this.decrementStep = decrementStep;
    }

    @Eg(given = {"Integer.MAX_VALUE"}, returns = "Integer.MIN_VALUE")
    @Eg(given = {"1"}, returns = "0")
    public int decrement(int a) {
        return a - decrementStep;
    }
}
