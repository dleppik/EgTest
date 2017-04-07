package com.vocalabs.egtest.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Sample code for writing GroovyInjector. */
public class GroovyExampleTest {

    @Test
    public void testVowels() {
        Object actual = execGroovy("GroovyExample.vowels(['Elephant', 'Octopus', 'Noodles'])");
        Object expected = execGroovy("['aEe', 'uOo', 'eo']");
        assertEquals(expected, actual);
    }

    private Object execGroovy(String code) {
        String script = "import com.vocalabs.egtest.example.*\n" + code;
        return new groovy.lang.GroovyShell().evaluate(script);
    }
}
