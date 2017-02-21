package com.vocalabs.egtest.example;

import org.junit.Test;

import static org.junit.Assert.*;


public class EgMatchExampleTest {

    @Test
    public void simpleEmailReEgMatches() throws Exception {
        assertTrue("dleppik@vocalabs.com", EgMatchExample.SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs.com").matches());
        assertTrue("dleppik@vocalabs.example.com", EgMatchExample.SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs.example.com").matches());
    }

    @Test
    public void simpleEmailReEgNoMatch() throws Exception {
        assertFalse("dleppik", EgMatchExample.SIMPLE_EMAIL_RE.matcher("dleppik").matches());
        assertFalse("dleppik@vocalabs@example.com", EgMatchExample.SIMPLE_EMAIL_RE.matcher("dleppik@vocalabs@example.com").matches());
        assertFalse("David Leppik <dleppik@vocalabs.com>", EgMatchExample.SIMPLE_EMAIL_RE.matcher("David Leppik <dleppik@vocalabs.com>").matches());
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