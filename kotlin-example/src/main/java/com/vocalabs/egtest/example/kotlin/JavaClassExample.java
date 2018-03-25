package com.vocalabs.egtest.example.kotlin;

import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;

/** Created to see if bugs in KotlinClassExample might be caused by kapt. */
public class JavaClassExample {
    public JavaClassExample(String... s) {}

    @EgMatch("dleppik@vocalabs.com")
    @EgMatch(value = "dleppik@vocalabs.example.com", construct = {"\"Hello\"", "\"World\""})

    @EgNoMatch("dleppik")
    @EgNoMatch(value = "dleppik@vocalabs@example.com", construct = {"\"A\""})
    @EgNoMatch(value = "David Leppik <dleppik@vocalabs.com>", construct = {"\"Hello\""})
    final Pattern simpleEmailRe = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");
}
