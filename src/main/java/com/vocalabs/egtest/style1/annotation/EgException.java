package com.vocalabs.egtest.style1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/** Indicates that, given the specified arguments, a Throwable is thrown. */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgExceptionContainer.class)
public @interface EgException {

    /** The arguments which cause an exception to be thrown. */
    String[] value();

    /**
     * If provided, indicates that the arguments from {@link #value()} will throw an instance
     * of {@code willThrow()} or a subclass of {@code willThrow()}.
     */
    Class<? extends Throwable> willThrow() default Throwable.class;
}
