package com.vocalabs.egtest.kotlinhandcraft

import org.junit.Test
import kotlin.test.*

internal class HelloTest {
    @Test
    fun testPositiveInput() {
        assertEquals(isPositive("2"), true)
        System.err.println("testing positiveInput")
    }

    @Test
    fun negativeInput() {
        assertEquals(isPositive("-2"), false)
    }
}