# EgTest
Use annotations to create unit tests and documentation in Java

###### Definition
e.g., _exempli gratia:_ for the sake of an example.

Not to be confused with i.e., _id et_: it is. (A definition or exhaustive list.)

## Why

Because unit tests should be the easiest way to test—even easier than a REPL.

Because code, tests, and documentation belong together.

## What it's for (and not for)
EgTest annotations provide **testable documentation in the source code**.

EgTest annotations automatically show up in Javadoc, so you can have confidence that the examples work as documented.

By removing the excuses for writing tests, you may find yourself writing tests for one-off code or exploratory code. 
Rather than testing in a REPL, it is easier to write a permanent unit test. Even simple one-line functions are worth a 
unit test when it's this quick and easy!

**EgTest is not an excuse to avoid writing full unit tests!** In some cases, especially with functional programming, 
you might be able to just use EgTest. EgTest should encourage you to break your complex code into small, simple, easily 
tested functions. With EgTest, you don't need to write several lines of unit test boilerplate for a one-line test—but 
you still need that boilerplate for more complicated tests.

### Examples

See [The example source code](https://github.com/dleppik/EgTest/tree/master/junit-example/src/main/java/com/vocalabs/egtest/example).

EgTest relies on repeatable annotations, which are a Java 8 feature.

```Java
package com.vocalabs.egtest.example;

import java.util.regex.Pattern;
import com.vocalabs.egtest.annotation.*;

public class ExampleForReadme {

    @Eg(given = {"\"World\""}, returns = "\"Hello, World!\"")
    public static String greet(String target) {
        return "Hello, "+target+"!";
    }


    // Package-private methods are fair game

    @Eg(given = {"1", "2"}, returns = "3")
    @Eg(given = {"1", "Integer.MAX_VALUE"}, returns = "Integer.MIN_VALUE")
    static int add(int a, int b) {
        return a + b;
    }


    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @EgMatch("dleppik@vocalabs.com")
    @EgMatch("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern
            SIMPLE_EMAIL_RE = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");

    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }


    // Non-static methods and variables are tested using the default zero-argument constructor

    @EgException({"null", "\"hello\""})
    @EgException({"\"hello\"", "null"})
    String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }


    // Floating-point types allow a delta; the default is 0.0.

    @Eg(given = {"1.0", "3.0"}, returns = "0.33333", delta = 0.001)
    @Eg(given = {"1.0", "0.0"}, returns = "Double.POSITIVE_INFINITY")
    public static double divide(double numerator, double divisor) {
        return numerator / divisor;
    }
}
```

##Details

Method parameters and return types should be constants, but they can be imported from anywhere, so long as 
it is visible to the test. Thus `org.apache.log4j.Level.DEBUG`, with the full package name, may be used if
log4j is included in the test build CLASSPATH.

The JUnit test generator copies parameters and return types directly into JUnit source code, so you can even make
fancier calls like `@Eg(given={"new StringBuilder(\"World\")"} returns="new StringBuilder(\"Hello, World!\")")`.
@Eg comparisons yield JUnit `assertEquals()` assertions, which compares objects by `Object.equals()`, so this example 
could succeed.

Any JVM language that supports assertions should in theory be able to use EgTest. In practice, a particular language 
may structure its code in ways that the test generator doesn't anticipate, or it might not support the Java annotation
processor which comes with EgTest.

##Generating JUnit Tests

Source code for JUnit tests are generated while compiling the main code. Generated classes have names ending in 
`$EgTest`, so they do not conflict with other JUnit tests.

To try it out, run `./gradlew ':junit-example:build'` from the EgTest directory. (Windows: `gradle.bat` should work.) 
Generated source code will be in `junit-example/build/generated/egTest` while JUnit test results will be at 
`junit-example/build/reports/tests/test/index.html`.
