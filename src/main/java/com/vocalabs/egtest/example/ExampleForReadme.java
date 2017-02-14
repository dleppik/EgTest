package com.vocalabs.egtest.example;

import java.util.regex.Pattern;
import com.vocalabs.egtest.annotation.*;

public class ExampleForReadme {

    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @EgMatches("dleppik@vocalabs.com")
    @EgMatches("dleppik@vocalabs.example.com")

    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern SIMPLE_EMAIL = Pattern.compile("...");

    @Eg(given = {"1", "2"}, returns = "3")
    @Eg(given = {"1", "Integer.MIN_VALUE"}, returns = "Integer.MAX_VALUE")
    public static int add(int a, int b) {
        return a + b;
    }

    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }

    @EgException({"null", "hello"})
    @EgException({"hello", "null"})
    public static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }
}
