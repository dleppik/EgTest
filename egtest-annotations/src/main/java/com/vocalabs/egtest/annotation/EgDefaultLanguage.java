package com.vocalabs.egtest.annotation;

import java.lang.annotation.*;

/**
 * Change the default {@link EgLanguage} within the current class. Does not affect subclasses.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface EgDefaultLanguage {
    EgLanguage value();
}
