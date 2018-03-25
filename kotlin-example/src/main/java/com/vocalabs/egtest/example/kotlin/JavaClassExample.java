package com.vocalabs.egtest.example.kotlin;

import com.vocalabs.egtest.annotation.EgMatch;

import java.util.regex.Pattern;

/** Created to see if bugs in KotlinClassExample might be caused by kapt. */
public class JavaClassExample {
    public JavaClassExample(String... s) {}

    @EgMatch("J-dleppik1@example.com")
    @EgMatch(value = "J-dleppik@example.com", construct = {"\"Hello\"", "\"World\""})
    @EgMatch(value = "J-dleppik@example.example.com", construct = {"\"Greetings\""})
    final Pattern simpleEmailRe = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");
}
