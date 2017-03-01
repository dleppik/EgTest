package com.vocalabs.egtest.testcase;

import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;

public class EgMatchesStatic {

    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    public static final Pattern
            NUMBER_RE = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    public static boolean isNumber(String s) { return NUMBER_RE.matcher(s).matches(); }

    /** A constructor that will cause attempts at construction to fail. */
    protected EgMatchesStatic(String thisConstructorExistsToEnsureNoInstantiation) {
        throw new UnsupportedOperationException(thisConstructorExistsToEnsureNoInstantiation);
    }
}
