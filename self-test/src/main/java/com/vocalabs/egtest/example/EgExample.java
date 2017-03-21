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



    /** This demonstrates the use of EgTest on long method names*/
    @Eg(returns = "\"\"")
    public String thisDemonstratesTheUseOfEgTestOnLongMethodNameswhatShouldWeNameOurMethodIAmSoConfusedddddddWillThisWorkIHopeItDoesWaitYouWantItEvenLongerrrrrrrrrrrrrrrrrrrr() {
        return "";
    }

    /** This demonstrates the use of EgTest on long parameter names */
    @Eg(given = {"56"}, returns = "0")
    @Eg(given = {"129"}, returns = "0")
    public int longParameterName(int thisDemonstratesTheUseOfEgTestOnLongParameterNameswhatShouldWeNameOurParamterIAmSoConfusedddddddWillThisWorkIHopeItDoesWaitYouWantItEvenLongerrrrrrrrrrrrrrrrrrrr) {
        return 0;
    }

    /** This demonstrates a long list of parameters */
    @Eg(given = {"1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5",
            "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5", "1", "2", "3", "4", "5",
            "1"}, returns = "0")
    public int longParameterList(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l,
                                 int m, int n, int o, int p, int q, int r, int s, int t, int u, int v, int w, int x,
                                 int y, int z, int aa, int bb, int cc, int dd, int ee, int ff, int gg, int hh, int ii,
                                 int jj, int kk, int ll, int mm, int oo, int pp, int qq, int rr, int ss, int tt, int uu,
                                 int vv, int ww, int xx, int yy, int zz) {
        return zz - a;
    }
}
