package com.vocalabs.egtest.writer.junit;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.vocalabs.egtest.writer.junit.TestWriter.*;
import static org.junit.Assert.*;

public class JUnitExampleWriterTest {

    /**
     * This is a perfect example of where I wish we could use EgTest within EgTest.
     */
    @Test
    public void testEscapeParameterNames() {
        List<String> args = Arrays.asList("java.lang.String", "double", "java.lang.Double[]", "org.omg.CORBA_2_3.ORB");
        String actual = escapeParameterNames(args);
        assertEquals("$java_lang_String$double$java_lang_Double$_A$org_omg_CORBA__2__3_ORB", actual);
    }
}