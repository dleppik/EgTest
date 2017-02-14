package com.vocalabs.egtest.style1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgContainer.class)
public @interface Eg {

    /** Method arguments, converted to the proper type  */
    String[] given() default {};
    String returns();

    /**
     * When the return type is a float or double, the maximum difference between {@link #returns()}
     * and the actual return value.
     */
    double delta() default 0.0;
}
