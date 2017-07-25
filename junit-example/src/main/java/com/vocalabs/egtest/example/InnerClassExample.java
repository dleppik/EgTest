package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgException;
import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;

public class InnerClassExample {

    public static class InnerStaticClass {

        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        public static final Pattern
                NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        @EgException(value = "null", willThrow = NullPointerException.class)
        @Eg(given = "\"-0.77E77\"", returns = "true")
        public static boolean number(String s) {
            return NUMBER_RE.matcher(s).matches();
        }

        private final int num;

        public InnerStaticClass() {
            this.num = 3;
        }

        public InnerStaticClass(int num) {
            this.num = num;
        }

        @Eg(returns = "3")
        @Eg(construct = "4", returns = "4")
        public int getNum() {
            return num;
        }

        public static class InnerInnerStaticClass {
            @EgMatch("-0.77E77")
            @EgNoMatch("-.Infinity")
            public static final Pattern
                    NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
        }


    }
}
