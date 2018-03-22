package com.vocalabs.egtest.annotation;

/**
 * The language used within an example. Java and Groovy may be used interchangeably, since Groovy tests are generated
 * using an interpreter within a Java source file.
 */
public enum EgLanguage {
    /** Use the default language specified at a higher level, typically JAVA. */
    INHERIT,
    /** Java, copied verbatim into test code. */
    JAVA,

    /**
     * Produce Groovy embedded in Java. May produce simple Java rather than embedding a Groovy interpreter for simple
     * cases such as primitives.
     */
    GROOVY,

    /** Produce Kotlin source code within Kotlin source files. Can't be mixed with JAVA or GROOVY within a file. */
    KOTLIN
}
