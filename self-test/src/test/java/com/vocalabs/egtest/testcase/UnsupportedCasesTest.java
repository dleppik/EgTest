package com.vocalabs.egtest.testcase;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Many of these tests ensure not that something fails, but that it fails via a compile-time message rather than in
 * a more cryptic way.
 */
public class UnsupportedCasesTest {

    private static String CLASS = "com.vocalabs.egtest.testcase.UnsupportedCases";
    private static String PATTERN = "java.util.regex.Pattern ";


    @Test
    public void innerStaticClassUnsupported() throws Exception {
        assertEquals("inner classes", Messages.unsupportedReason(PATTERN + CLASS + ".InnerStaticClass#NUMBER_RE"));
        assertEquals("inner classes", Messages.unsupportedReason(PATTERN + CLASS + ".InnerStaticClass.InnerInnerStaticClass#NUMBER_RE"));
        assertEquals("inner classes", Messages.unsupportedReason(PATTERN + CLASS + ".InnerClass#numberRe"));
    }

    @Test
    public void privateUnsupported() {
        assertEquals("private or protected", Messages.unsupportedReason(PATTERN+CLASS+"#NUMBER_RE"));
    }

    @Test
    public void protectedUnsupported() {
        assertEquals("private or protected", Messages.unsupportedReason(PATTERN+CLASS+"#COW_RE"));
    }
}
