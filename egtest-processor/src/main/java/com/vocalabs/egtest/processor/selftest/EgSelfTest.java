package com.vocalabs.egtest.processor.selftest;

import com.vocalabs.egtest.processor.Settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For testing EgTest: marks that the other annotations should fail in an interesting way. The existence of this
 * annotation should cause a compile failure unless {@link Settings#SELF_TEST_KEY} is set to true.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
public @interface EgSelfTest {
    ExpectedBehavior value();
}
