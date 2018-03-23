package com.vocalabs.egtest.writer.junit;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.type.TypeMirror;

/** Used by {@link CodeInjector} to embed a particular language into Java source code. */
public interface LanguageInjector {

    /** Convert egText into Java and inject it into specBuilder. */
    void add(MethodSpec.Builder specBuilder, TypeMirror type, String egText);

    /**
     * Called after all calls to {@link #add(MethodSpec.Builder, TypeMirror, String)} to add methods, constants,
     * or anything else the class needs to have.
     */
    void decorateClass(TypeSpec.Builder toAddTo);
}
