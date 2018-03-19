package com.vocalabs.egtest.kotlinhandcraft

import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

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