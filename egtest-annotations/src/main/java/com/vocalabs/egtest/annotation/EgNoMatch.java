package com.vocalabs.egtest.annotation;

import java.lang.annotation.*;

/**
 * Indicates that {@link EgMatches} would fail.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgNoMatchContainer.class)

public @interface EgNoMatch {
    String value();
}
