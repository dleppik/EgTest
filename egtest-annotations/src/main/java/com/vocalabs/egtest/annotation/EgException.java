package com.vocalabs.egtest.annotation;

import java.lang.annotation.*;

/** Indicates that, given the specified arguments, a Throwable is thrown. */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgExceptionContainer.class)
public @interface EgException {

    /** Constructor arguments */
    String[] construct() default {};

    /** The arguments which cause an exception to be thrown. */
    String[] value();

    /**
     * If provided, indicates that the arguments from {@link #value()} will throw an instance
     * of {@code willThrow()} or a subclass of {@code willThrow()}.
     */
    Class<? extends Throwable> willThrow() default Throwable.class;
}
