package com.vocalabs.egtest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Asserts that the given arguments return the specified value.
 *
 * <p>
 *     Method arguments and return values should be constants (written as Java source code) for a test class in the
 *     same package as the class being tested. If a non-static method is tested, the test class uses the default
 *     constructor.
 * </p>
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
@Repeatable(EgContainer.class)
public @interface Eg {

    String[] given() default {};
    String returns();

    /**
     * When the return type is a float or double, the maximum difference between {@link #returns()}
     * and the actual return value.
     */
    double delta() default 0.0;
}
