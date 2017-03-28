package com.vocalabs.egtest.annotation;

import java.lang.annotation.*;

/**
 * Indicates that {@link EgMatch} would fail.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgNoMatchContainer.class)

public @interface EgNoMatch {

    /** Constructor arguments */
    String[] construct() default {};

    String value();
}
