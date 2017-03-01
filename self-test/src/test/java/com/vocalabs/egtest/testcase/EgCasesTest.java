package com.vocalabs.egtest.testcase;

import org.junit.Test;

import static org.junit.Assert.*;

public class EgCasesTest {

    @Test
    public void add() throws Exception {
        assertEquals("add(2, 1)", 3, EgCases.add(2, 1));
        assertEquals("add(-1, Integer.MIN_VALUE)", Integer.MAX_VALUE, EgCases.add(-1, Integer.MIN_VALUE));
    }

    @Test
    public void greet() throws Exception {
        assertEquals("greet(\"World\")", "Hello, World!", EgCases.greet("World"));
    }

    @Test
    public void divideA() throws Exception {
        assertEquals("divideA(1.0, 2.0)", 0.5, EgCases.divide(1.0, 2.0), 0.0);
        assertEquals("divideA(1.0, 3.0)", 0.33333, EgCases.divide(1.0, 3.0), 0.001);
        assertEquals("divideA(1.0, 0.0)", Double.POSITIVE_INFINITY, EgCases.divide(1.0, 0.0), 0.0);
        assertEquals("divideA(0.0, 0.0)", Double.NaN, EgCases.divide(0.0, 0.0), 0.0);
    }

    @Test
    public void divideB() throws Exception {
        assertEquals("divideB(1.0, 2.0)", (Double) 0.5, EgCases.divide((Double) 1.0, (Double) 2.0));
        assertEquals("divideB(1.0, 3.0)", 0.33333, EgCases.divide((Double) 1.0, (Double) 3.0), 0.001);
        assertEquals("divideB(1.0, 0.0)", 0.5, EgCases.divide((Double) 1.0, (Double) 0.0), Double.POSITIVE_INFINITY);
        assertEquals("divideB(0.0, 0.0)", (Double) Double.NaN, EgCases.divide((Double) 0.0, (Double) 0.0));
    }

    @Test
    public void decrement() throws Exception {
        assertEquals("decrement(Integer.MIN_VALUE)", Integer.MAX_VALUE, instance().decrement(Integer.MIN_VALUE));
        assertEquals("decrement(1)", 0, instance().decrement(1));
    }

    private EgCases instance() { return new EgCases(); }
}