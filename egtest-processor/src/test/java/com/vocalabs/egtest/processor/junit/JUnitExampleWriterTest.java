package com.vocalabs.egtest.processor.junit;

import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class JUnitExampleWriterTest {

    /**
     * This is a perfect example of where I wish we could use EgTest within EgTest.
     */
    @Test
    public void testEscapeParameterNames() throws Exception {


        Stream<String> args = Stream.of("java.lang.String", "double", "java.lang.Double[]", "org.omg.CORBA_2_3.ORB");
        String actual = TestWriter.escapeParameterNames(args);
        assertEquals("$java_lang_String$double$java_lang_Double$_A$org_omg_CORBA__2__3_ORB", actual);
    }
}