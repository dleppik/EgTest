package com.vocalabs.egtest.example.kotlin

import org.junit.Test
import org.junit.Assert.*
import org.junit.Ignore

/**
 * Hand-written tests for Example
 */
class DefaultLanguageAsJavaExampleTest {

    /** This must be updated every time we change ExampleForReadme or if we change how annotations map to tests.  */
    @Test
    @Ignore
    fun generatedTestsExist() {
        fail("Not written")
        /*
        val generatedClass = javaClass.getClassLoader().loadClass("com.vocalabs.egtest.example.kotlin.DefaultLanguageAsJavaExample\$EgTest")
        assertHasTestMethod("testReturns\$greet", generatedClass)
        assertHasTestMethod("testReturns\$add", generatedClass)
        assertHasTestMethod("testMatch\$SIMPLE_EMAIL_RE", generatedClass)
        assertHasTestMethod("testMatch\$validEmail", generatedClass)
        assertHasTestMethod("testException\$methodWhichCannotHandleNulls", generatedClass)
        assertHasTestMethod("testException\$anotherMethodWhichCannotHandleNulls", generatedClass)
        assertHasTestMethod("testReturns\$divide", generatedClass)
        assertHasTestMethod("testMatch\$startsWithAsciiVowel", generatedClass)
        assertHasTestMethod("testReturns\$startsWithAsciiVowel", generatedClass)
        assertHasTestMethod("testException\$startsWithAsciiVowel", generatedClass)
        */
    }

    /*
    private fun assertHasTestMethod(name: String, generatedClass: Class<*>) {
        val method = generatedClass.getMethod(name)
        assertNotNull(name, method.getAnnotation<Test>(Test::class.java!!))
    }
    */
}