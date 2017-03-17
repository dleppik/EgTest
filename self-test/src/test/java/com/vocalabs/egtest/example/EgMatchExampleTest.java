package com.vocalabs.egtest.example;

import org.junit.Test;

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
    public void isEmailEgMatches() throws Exception {
        assertTrue("dleppik@vocalabs.com", EgMatchExample.isEmail("dleppik@vocalabs.com"));
        assertTrue("dleppik@vocalabs.example.com", EgMatchExample.isEmail("dleppik@vocalabs.example.com"));
    }

    @Test
    public void isEmailEgNoMatch() throws Exception {
        assertFalse("dleppik", EgMatchExample.isEmail("dleppik"));
        assertFalse("dleppik@vocalabs@example.com", EgMatchExample.isEmail("dleppik@vocalabs@example.com"));
        assertFalse("David Leppik <dleppik@vocalabs.com>", EgMatchExample.isEmail("David Leppik <dleppik@vocalabs.com>"));
    }
}