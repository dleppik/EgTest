package com.vocalabs.egtest.example.kotlin

import com.vocalabs.egtest.annotation.EgMatch

class KotlinClassExample(vararg val s: String) {

    @EgMatch("K-dleppik1@example.com")
    @EgMatch(value = "K-dleppik@example.com", construct = ["\"Hello\"", "\"World\""])
    @EgMatch(value = "K-dleppik@example.example.com", construct = ["\"Greetings\""])
    val simpleEmailRe = """^[\w+.\-=&|/?!#$*]+@[\w.\-]+\.[\w]+$""".toRegex()

    @EgMatch("all of these have construct values", construct = ["Yo"])
    @EgMatch("all of these have construct values", construct = ["Mama"])
    val generalRe = """.*""".toRegex()
}