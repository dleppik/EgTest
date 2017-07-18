package com.vocalabs.egtest.example.groovy;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Hand-written tests for Example
 */
public class DefaultLanguageAsJavaExampleTest {

    /** This must be updated every time we change ExampleForReadme or if we change how annotations map to tests. */
    @Test
    public void generatedTestsExist() throws Exception {
        Class<?> generatedClass = getClass().getClassLoader().loadClass("com.vocalabs.egtest.example.groovy.DefaultLanguageAsJavaExample$EgTest");
        assertHasTestMethod("testReturns$greet", generatedClass);
        assertHasTestMethod("testReturns$add", generatedClass);
        assertHasTestMethod("testMatch$SIMPLE_EMAIL_RE", generatedClass);
        assertHasTestMethod("testMatch$validEmail", generatedClass);
        assertHasTestMethod("testException$methodWhichCannotHandleNulls", generatedClass);
        assertHasTestMethod("testException$anotherMethodWhichCannotHandleNulls", generatedClass);
        assertHasTestMethod("testReturns$divide", generatedClass);
        assertHasTestMethod("testMatch$startsWithAsciiVowel", generatedClass);
        assertHasTestMethod("testReturns$startsWithAsciiVowel", generatedClass);
        assertHasTestMethod("testException$startsWithAsciiVowel", generatedClass);
    }

    private void assertHasTestMethod(String name, Class<?> generatedClass) throws NoSuchMethodException {
        Method method = generatedClass.getMethod(name);
        assertNotNull(name, method.getAnnotation(Test.class));
    }
}