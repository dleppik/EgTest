package com.vocalabs.egtest.example.kotlin

import com.vocalabs.egtest.annotation.Eg

class KotlinExample {
    @Eg(given = arrayOf("listOf()"), returns = "listOf()")
    fun vowels(words: Collection<String>): Collection<String> {
        TODO()
    }
}