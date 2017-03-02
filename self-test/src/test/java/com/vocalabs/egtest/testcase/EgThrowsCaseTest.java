package com.vocalabs.egtest.testcase;

import org.junit.Test;

import static org.junit.Assert.*;

public class EgThrowsCaseTest {
    @Test
    public void testThrowsOnNull() throws Exception {
        try {
            EgThrowsCase.throwsOnNull(null);
        }
        catch (Throwable ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }

}