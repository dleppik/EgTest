package com.vocalabs.egtest.processor.selftest;

/** For testing EgTest: marks that the other annotations should fail in an interesting way. */
public @interface EgSelfTest {
    ExpectedBehavior value();
}
