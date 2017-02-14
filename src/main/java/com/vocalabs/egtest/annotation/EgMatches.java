package com.vocalabs.egtest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Annotates an example where a regular expression matches or a method/function which takes a String and returns a
 * boolean
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgMatchesContainer.class)
public @interface EgMatches {
    String value();
}
