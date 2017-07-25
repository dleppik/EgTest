package com.vocalabs.egtest.testcase;


import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.selftest.EgSelfTest;
import com.vocalabs.egtest.processor.selftest.ExpectedBehavior;

import java.util.regex.Pattern;

public class UnsupportedCases {

    /** Private properties are off limits. */
    @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    private static final Pattern
            NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

    /** Protected properties are unsupported. */
    @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
    @EgMatch("Cow")
    @EgNoMatch("Cw")
    protected static final Pattern COW_RE = Pattern.compile("C?w");

    /** Inner classes must be static, otherwise how would we construct them? */
    public class InnerClass {

        @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
        @EgMatch("World")
        @EgNoMatch("Planet")
        public boolean isWorld(String s) {
            return "World".equals(s);
        }

        @EgSelfTest(ExpectedBehavior.UNSUPPORTED_CASE)
        @EgMatch("-0.77E77")
        @EgNoMatch("-.Infinity")
        public final Pattern
                numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");
    }
}
