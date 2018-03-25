package com.vocalabs.egtest.example.kotlin

import com.vocalabs.egtest.annotation.EgMatch
import com.vocalabs.egtest.annotation.EgNoMatch

class KotlinClassExample(vararg val s: String) {

    @EgMatch("dleppik@vocalabs.com")
    @EgMatch(value = "dleppik@vocalabs.example.com", construct = ["\"Hello\"", "\"World\""])

    @EgNoMatch("dleppik")
    @EgNoMatch(value = "dleppik@vocalabs@example.com", construct = ["\"A\""])
    @EgNoMatch(value = "David Leppik <dleppik@vocalabs.com>", construct = ["\"Hello\""])
    val simpleEmailRe = """^[\w+.\-=&|/?!#$*]+@[\w.\-]+\.[\w]+$""".toRegex()
}