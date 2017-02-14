package com.vocalabs.egtest.style1.example;

import com.vocalabs.egtest.style1.annotation.EgMatches;
import com.vocalabs.egtest.style1.annotation.EgNoMatch;

import java.util.regex.Pattern;

public class EgMatchExample {
    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @EgMatches("dleppik@vocalabs.com")
    @EgMatches("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern
            SIMPLE_EMAIL_RE = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");


    /** Method wrapper for {@link EgMatchExample#SIMPLE_EMAIL_RE}. */
    @EgMatches("dleppik@vocalabs.com")
    @EgMatches("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public boolean isEmail(String email) {
        return SIMPLE_EMAIL_RE.matcher(email).matches();
    }
}
