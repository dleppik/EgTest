package com.vocalabs.egtest.testcase;


import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;

public class EgMatchesCase {
    @EgMatches("-0.77E77")
    @EgNoMatch("-.Infinity")
    private static final Pattern
            NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

    public static final Object AN_OBJECT = new Object() {
        @EgMatches("-0.77E77")
        @EgNoMatch("-.Infinity")
        public final Pattern
                numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
    };

    public static class InnerStaticClass {
        @EgMatches("-0.77E77")
        @EgNoMatch("-.Infinity")
        private static final Pattern
                NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");


        public static class InnerInnerStaticClass {
            @EgMatches("-0.77E77")
            @EgNoMatch("-.Infinity")
            private static final Pattern
                    NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
        }
    }

    public class InnerClass {
        @EgMatches("-0.77E77")
        @EgNoMatch("-.Infinity")
        public final Pattern
                numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
    }
}
