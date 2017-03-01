package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;


public class EgMatchExample {
    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @EgMatch("dleppik@vocalabs.com")
    @EgMatch("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern
            SIMPLE_EMAIL_RE = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");

    /** Method wrapper for {@link EgMatchExample#SIMPLE_EMAIL_RE}. */
    @EgMatch("dleppik@vocalabs.com")
    @EgMatch("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static boolean isEmail(String email) { return SIMPLE_EMAIL_RE.matcher(email).matches(); }


    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    public final Pattern
            numberRe = Pattern.compile("(?:NaN|-?(?:(?:\\d+|\\d*\\.\\d+)(?:[E|e][+|-]?\\d+)?|Infinity))");

    @EgMatch("-0.77E77")
    @EgNoMatch("-.Infinity")
    public boolean isNumber(String s) {
        return numberRe.matcher(s).matches();
    }
}
