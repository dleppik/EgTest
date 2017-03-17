package com.vocalabs.egtest.testcase;

import com.vocalabs.egtest.annotation.Eg;

public class EgCases {

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
    public static double divide(double numerator, double denominator) {
        return numerator / denominator;
    }

    @Eg(given = {"1.0", "2.0"}, returns = "0.5")
    @Eg(given = {"1.0", "3.0"}, returns = "0.33333", delta = 0.001)
    @Eg(given = {"1.0", "0.0"}, returns = "Double.POSITIVE_INFINITY")
    @Eg(given = {"0.0", "0.0"}, returns = "Double.NaN")
    public static Double divide(Double numerator, Double denominator) {
        return numerator / denominator;
    }

    // When testing a non-static method, the default constructor is used
    private final int decrementStep;

    EgCases() {
        this(1);
    }

    EgCases(int decrementStep) {
        this.decrementStep = decrementStep;
    }

    @Eg(given = {"Integer.MIN_VALUE"}, returns = "Integer.MAX_VALUE")
    @Eg(given = {"1"}, returns = "0")
    int decrement(int a) {
        return a - decrementStep;
    }

    /** A trivial case, to show that it works with overridden methods. */
    @Eg(returns = "1")
    @Override
    public int hashCode() {
        return decrementStep;
    }
}
