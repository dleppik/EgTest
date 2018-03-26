package com.vocalabs.egtest.example.kotlin

import com.vocalabs.egtest.annotation.EgMatch
import com.vocalabs.egtest.annotation.EgNoMatch

class KotlinClassExample(vararg val s: String) {

    // Note: to have a non-empty "construct", the first annotation must be non-empty. Kapt bug KT-23427
    @EgMatch(value = "dleppik@vocalabs.example.com", construct = ["\"Hello\"", "\"World\""])
    @EgMatch(value = "dleppik@vocalabs.com", construct = [])
    @EgNoMatch(value = "dleppik@vocalabs@example.com", construct = ["\"A\""])
    @EgNoMatch(value = "David Leppik <dleppik@vocalabs.com>", construct = ["\"Hello\""])
    @EgNoMatch(value = "dleppik", construct = [])
    val simpleEmailRe = """^[\w+.\-=&|/?!#$*]+@[\w.\-]+\.[\w]+$""".toRegex()

    @EgMatch(value = "dleppik@vocalabs.example.com", construct = ["\"Hello\"", "\"World\""])
    @EgMatch(value = "dleppik@vocalabs.com", construct = [])
    @EgNoMatch(value = "dleppik@vocalabs@example.com", construct = ["\"A\""])
    @EgNoMatch(value = "David Leppik <dleppik@vocalabs.com>", construct = ["\"Hello\""])
    @EgNoMatch(value = "dleppik", construct = [])
    fun isEmail(s: String): Boolean = simpleEmailRe.matches(s)
}