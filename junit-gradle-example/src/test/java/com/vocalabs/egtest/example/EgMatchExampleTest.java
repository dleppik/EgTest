package com.vocalabs.egtest.example;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import static com.vocalabs.egtest.example.EgMatchExample.SIMPLE_EMAIL_RE;
import static org.junit.Assert.*;


public class EgMatchExampleTest {

    @Test
    public void simpleEmailReEgMatches() throws Exception {
        assertTrue("dleppik@vocalabs.com", SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs.com").matches());
        assertTrue("dleppik@vocalabs.example.com", SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs.example.com").matches());
    }

    @Test
    public void simpleEmailReEgNoMatch() throws Exception {
        assertFalse("dleppik", SIMPLE_EMAIL_RE.matcher("dleppik").matches());
        assertFalse("dleppik@vocalabs@example.com", SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs@example.com").matches());
        assertFalse("David Leppik <dleppik@vocalabs.com>", SIMPLE_EMAIL_RE.matcher("David Leppik <dleppik@vocalabs.com>").matches());
    }

    @Test
    public void numberReEgMatches() throws Exception {
        assertTrue("-0.77E77", matchSimpleEmailRe("-0.77E77"));
    }

    @Test
    public void numberReEgNoMatch() throws Exception {
        assertFalse("-.Infinity", matchSimpleEmailRe("-.Infinity"));
    }


    private boolean matchSimpleEmailRe(String s) throws Exception {
        Field f = EgMatchExample.class.getDeclaredField("NUMBER_RE");
        f.setAccessible(true);
        Pattern p = (Pattern) f.get(null);
        return p.matcher(s).matches();
    }

    @Test
    public void isEmailEgMatches() throws Exception {
        assertTrue("dleppik@vocalabs.com", instance().isEmail("dleppik@vocalabs.com"));
        assertTrue("dleppik@vocalabs.example.com",instance().isEmail("dleppik@vocalabs.example.com"));
    }

    @Test
    public void isEmailEgNoMatch() throws Exception {
        assertFalse("dleppik", instance().isEmail("dleppik"));
        assertFalse("dleppik@vocalabs@example.com", instance().isEmail("dleppik@vocalabs@example.com"));
        assertFalse("David Leppik <dleppik@vocalabs.com>", instance().isEmail("David Leppik <dleppik@vocalabs.com>"));
    }

    private EgMatchExample instance() { return new EgMatchExample(); }
}