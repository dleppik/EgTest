package com.vocalabs.egtest.style1.example;

import com.vocalabs.egtest.style1.annotation.EgException;

public class EgExceptionExample {

    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }

    @EgException({"null", "hello"})
    @EgException({"hello", "null"})
    public static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        if (thing1 == null) {
            throw new IllegalArgumentException("thing1 is null, but I just don't feel like throwing a NPE");
        }
        return thing1.toString() + thing2.toString();
    }
}
