package com.vocalabs.egtest.annotation;

/** The language used within an example when it is not a simple primitive. */
public enum EgLanguage {
    /** Use the default language specified at a higher level, typically JAVA. */
    INHERIT,
    /** Java, copied verbatim into test code. */
    JAVA,
    GROOVY
}
