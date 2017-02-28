package com.vocalabs.egtest.processor.selftest;

/** Describes what would happen if {@link EgSelfTest} were missing. */
public enum ExpectedBehavior {
    CREATE_FAILING_TEST,
    UNSUPPORTED_CASE,
    BUILD_FAILS
}
