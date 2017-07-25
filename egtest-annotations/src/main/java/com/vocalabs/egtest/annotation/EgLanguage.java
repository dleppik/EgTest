package com.vocalabs.egtest.annotation;

/**
 * The language used within an example. Substitutions may be made to make tests easier to read. For example,
 * when an example contains a simple primitive, the primitive may be used verbatim instead of embedding a Groovy
 * interpreter inside a Java test case.
 */
public enum EgLanguage {
    /** Use the default language specified at a higher level, typically JAVA. */
    INHERIT,
    /** Java, copied verbatim into test code. */
    JAVA,
    GROOVY
}
