package com.vocalabs.egtest.testcase;

import com.vocalabs.egtest.annotation.EgException;

public class EgThrowsCase {

    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static int throwsOnNull(String s) {
        return s.length();
    }

    @EgException(value = {"\"a\"", "\"moXie\"", "\"b\""}, willThrow = IllegalArgumentException.class)
    public void cantHandleX(String... strings) {
        for (String s : strings) {
            if (s.contains("X"))
                throw new IllegalArgumentException("Can't handle X in "+s);
        }
    }
}
