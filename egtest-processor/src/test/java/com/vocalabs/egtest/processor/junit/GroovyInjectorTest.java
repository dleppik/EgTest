package com.vocalabs.egtest.processor.junit;

import org.junit.Test;

import javax.lang.model.type.TypeKind;

import static org.junit.Assert.*;


public class GroovyInjectorTest {
    @Test
    public void plainJavaChar() throws Exception {
        assertTrue(GroovyInjector.plainJava(TypeKind.CHAR, "'a'"));
        assertTrue(GroovyInjector.plainJava(TypeKind.CHAR, "'\''"));
        assertTrue(GroovyInjector.plainJava(TypeKind.CHAR, "'\t'"));
    }

    @Test
    public void plainJavaInt() throws Exception {
        assertFalse(GroovyInjector.plainJava(TypeKind.INT, "0xyz"));
        assertTrue(GroovyInjector.plainJava(TypeKind.INT, "0x12f"));
    }
}