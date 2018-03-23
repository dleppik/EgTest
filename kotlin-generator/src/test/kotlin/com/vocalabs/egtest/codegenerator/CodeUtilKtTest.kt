package com.vocalabs.egtest.codegenerator

import org.junit.Assert.*
import org.junit.Test

class CodeUtilKtTest {
    @Test
    fun testAnnotationToStringWithBody() {
        val expected = """@EgMatches("Hello")"""
        assertEquals(expected, annotationToString("EgMatches", "\"Hello\""))
    }

    @Test
    fun testAnnotationToStringWithoutBody() {
        val expected = """@Deprecated"""
        assertEquals(expected, annotationToString("Deprecated", null))
    }

}