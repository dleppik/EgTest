package com.vocalabs.egtest.style1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Indicates that {@link EgMatches} would fail.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgNoMatchContainer.class)

public @interface EgNoMatch {
    String value();
}
