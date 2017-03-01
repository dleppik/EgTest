package com.vocalabs.egtest.testcase;


import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.selftest.EgSelfTest;
import com.vocalabs.egtest.processor.selftest.ExpectedBehavior;

import java.util.regex.Pattern;

public class EgMatchesCase {
    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    private static final Pattern
            NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

    public static final Object AN_OBJECT = new Object() {
        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        public final Pattern
                numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
    };


    /**
     * We don't currently support inner classes, but the build should not fail unless it is configured to fail on
     * unsupported cases. We want to allow people to build tests in anticipation of future functionality.
     */
    public static class InnerStaticClass {
        @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        private static final Pattern
                NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");


        public static class InnerInnerStaticClass {
            @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
            @EgMatch("-0.77E77")
            @EgNoMatch("-.Infinity")
            private static final Pattern
                    NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
        }
    }

    /**
     * We don't currently support inner classes, but the build should not fail unless it is configured to fail on
     * unsupported cases.  We want to allow people to build tests in anticipation of future functionality.
     */
    public class InnerClass {
        @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        public final Pattern
                numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
    }
}
