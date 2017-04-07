package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.type.TypeMirror;

/** Inserts plain Java. */
public class JavaInjector implements LanguageInjector {
    @Override
    public void add(MethodSpec.Builder specBuilder, TypeMirror type, String egText) {
        specBuilder.addCode("$L", egText);
    }

    @Override
    public void decorateClass(TypeSpec.Builder toAddTo) {
    }
}
