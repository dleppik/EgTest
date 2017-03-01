package com.vocalabs.egtest.annotation;

import java.lang.annotation.*;

/**
 * Annotates an example where a regular expression matches or a method/function which takes a String and returns a
 * boolean.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgMatchContainer.class)
public @interface EgMatch {
    String value();
}
