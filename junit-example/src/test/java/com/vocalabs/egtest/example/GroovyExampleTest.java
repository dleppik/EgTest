package com.vocalabs.egtest.example;

import com.vocalabs.egtest.language.GroovySupport;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroovyExampleTest {

    private final List<String> imports = Collections.singletonList("com.vocalabs.egtest.example.*");

    @Test
    public void testVowels() {
        Object actual = groovyEval("GroovyExample.vowels(['Elephant', 'Octopus', 'Noodles'])");
        Object expected = groovyEval("['aEe', 'uOo', 'eo']");
        assertEquals(expected, actual);
    }

    private Object groovyEval(String text) {
        return GroovySupport.getInstance().eval(imports, text);
    }
}
