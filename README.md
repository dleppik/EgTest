# EgTest
Use annotations to create unit tests and documentation in Java

###### Definition
e.g., _exempli gratia:_ for the sake of an example.  (Not to be confused with i.e., _id et_: it is.)

For example:

```Java
    @Eg(given = {"1", "2"}, returns = "3")
    @Eg(given = {"1", "Integer.MAX_VALUE"}, returns = "Integer.MIN_VALUE")
    int add(int a, int b) {
        return a + b;
    }
```

The `@Eg` annotations tell the EgTest annotation processor to create unit tests from the method and its two examples.
They also show up in Javadoc. That way your examples are always in synch with the documentation, and you see
the tests every time you look at the source code.

Have you ever written a regular expression, tested it in a regex checker or a REPL, and then forgotten to write unit
tests for all the cases you hand tested? Sure unit tests are easy to write, but what if they were __so easy to write
that you didn't bother with hand-testing?__ Like this:

```Java
    @EgMatch("dleppik@vocalabs.com")
    @EgMatch("dleppik@vocalabs.example.com")
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern
            SIMPLE_EMAIL_RE = Pattern.compile("^[\\w+.\\-=&|/?!#$*]+@[\\w.\\-]+\\.[\\w]+$");
```

## What it's for (and not for)
EgTest annotations provide **testable documentation in the source code**. The annotations automatically show up in 
Javadoc and are automatically compiled into JUnit tests. That way, you know that the examples work.

By removing the excuses for writing tests, you may find yourself writing tests for one-off code or exploratory code. 
Rather than testing in a REPL, it is easier to write a permanent unit test. Even simple one-line functions are worth a 
unit test when it's this quick and easy!

### EgTest is not an excuse to avoid writing full unit tests

In some cases, especially with functional programming, 
EgTest may be sufficient. EgTest should encourage you to break your complex code into small, simple, easily 
tested functions. But what makes EgTest so simple is that it doesn't try to solve all your testing needs: it's there
to complement, not replace, full unit testing.

### EgTest is not a static analysis or type constraint tool

There are other similar-looking annotations out there which extend the type system by 
describing constraints, including 
[several to describe nullability](https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use).
Some of them even [enforce the constraints through static analysis](https://checkerframework.org/).
These are extremely powerful, since they cover entire classes of errors. EgTest isn't one of them.

EgTest is for writing **examples**. Examples are specific, concrete, and easier to understand—especially in complex cases.
It should be used with type-constraint annotations, dealing with the cases that they can't handle or providing 
clarification.

### Examples

See [the example source code](https://github.com/dleppik/EgTest/tree/master/junit-example/src/main/java/com/vocalabs/egtest/example).

EgTest relies on repeatable annotations, which are a Java 8 feature.

```Java
package com.vocalabs.egtest.example;

import java.util.regex.Pattern;
import com.vocalabs.egtest.annotation.*;

public class ExampleForReadme {

    //
    // @Eg:  given the specified input, returns the specified value. Arguments are copied verbatim into test code,
    // so we need to escape strings, since there could be a class named "World" in the test's scope.
    //

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

    // Floating-point return types have a delta (margin of error); the default is 0.0.

    @Eg(given = {"1.0", "3.0"}, returns = "0.33333", delta = 0.001)
    @Eg(given = {"1.0", "0.0"}, returns = "Double.POSITIVE_INFINITY")
    static double divide(double numerator, double divisor) {
        return numerator / divisor;
    }

    //
    // @EgMatch/@EgNoMatch: String pattern matching, for regular expressions or boolean functions
    //

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

    /** Boolean function wrapping {@link #SIMPLE_EMAIL_RE} */
    @EgMatch("dleppik@vocalabs.com")
    @EgNoMatch("dleppik")
    public static boolean validEmail(String email) {
        return SIMPLE_EMAIL_RE.matcher(email).matches();
    }

    //
    // @EgException: for when failure is an option
    //

    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }

    // If you don't specify what it throws, the test passes if any Throwable is thrown.

    @EgException({"null", "\"hello\""})
    @EgException({"\"hello\"", "null"})
    static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }


    //
    // Non-static usage:
    // Non-static methods and variables are tested using the default zero-argument constructor
    // unless constructor arguments are specified
    //

    private final int min;
    private final int max;

    public ExampleForReadme(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public ExampleForReadme() {
        this(0, 5);
    }

    @Eg(given = "4", returns = "true")
    @Eg(construct = {"8", "9"},
        given = "4",
        returns = "false")
    boolean inRange(int num) {
        return num >= min  &&  num < max;
    }

    //
    // Putting it all together
    //

    /**
     * Return true if the string starts with one of the four ASCII vowels (not including Y).
     * @param s a non-null String with at least one character.
     */
    @EgMatch("Alaska")
    @Eg(given = "\"Alaska\"", returns = "true") // Same as above
    @EgMatch("elephant")
    @EgMatch("I")
    @EgMatch("October")
    @EgMatch("underground")
    @EgNoMatch("yes")
    @EgNoMatch("æon")
    @EgException({"\"\""}) // Empty string, throws something (we don't care what)
    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    boolean startsWithAsciiVowel(String s) {
        switch (s.toLowerCase().charAt(0)) {
            case 'a': return true;
            case 'e': return true;
            case 'i': return true;
            case 'o': return true;
            case 'u': return true;
            default:  return false;
        }
    }
}
```

## How does it work?

At compile time, EgTest's annotation processor creates JUnit tests separate from your hand-written unit tests.
With parameters `given`, `returns`, and `construct` it copies Java code verbatim. Thus you can use fully qualified
names, e.g. `@Eg(returns=java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)`. Just like regular JUnit tests, the 
tests are in the same package as the class being tested. For example: 
`@Eg(given={"new MyClassInTheSamePackage(\"World\")"} returns="new MyClassInTheSamePackage(\"Hello, World!\")")`.

Source code for JUnit tests are generated while compiling the main code. Generated classes have names ending in 
`$EgTest`, so they do not conflict with other JUnit tests.

## Groovy for easier-to-read examples

By default, Java is used whenever code is copied verbatim. This makes the generated source code easier to follow,
but all those escaped quotes can be a pain, and lists are difficult to construct. Fortunately, you can change EgTest's
language from Java to Groovy.

Groovy lets you use single quotes to construct strings, and lists are as easy as `[1, 2, 3]`. For example:

```Java
@Eg(language = EgLanguage.GROOVY, given = {"['apple', 'banjo', 'cow']"}, returns = "'apple banjo cow'")
public static String joinWithSpace(java.util.List<String> items) {
    return String.join(" ", items);
}
```

As you can see, you can set the language in an annotation with `language = EgLanguage.GROOVY`. You can also set the
language for an entire class (and its inner classes) like this:

```Java
@EgDefaultLanguage(EgLanguage.GROOVY)
public class NestedGroovyExampleForReadme {
    @Eg(given = "['this', 'is', 'groovy']", returns = "'this is groovy'")
    public static String joinWithSpace(java.util.List<String> items) {
        return String.join(" ", items);
    }

    // Inner classes continue to use Groovy
    public static class Inner {
        @Eg(given = "['this', 'is', 'groovy']", returns = "'this-is-groovy'")
        public static String joinWithDash(java.util.List<String> items) {
            return String.join("-", items);
        }
    }
}
```

To set the language for the entire build, add the `-Aegtest.targetLanguage=GROOVY` compiler argument. In Gradle, that 
looks like this:

```
compileJava.options.compilerArgs.add("-Aegtest.targetLanguage=GROOVY")
``` 

At the moment, Groovy only works for `given` and `returns`. Java is used to construct an object.

In Groovy mode, the unit test methods are still written in Java, but the appropriate portions call out to a Groovy 
interpreter. Trivial cases which are identical in both languages, such as numerical literals, are handled with pure 
Java.

## Don't annotate anonymous classes!

Because anonymous classes are constructed at runtime, 
annotations within anonymous classes are not visible to the compiler. Not only is EgTest unable to create a test, it
can't even emit a warning! As is true whenever you write unit tests, **you should write a 
failing test—and confirm that it fails—before making it pass.**

```Java
public class BadExample {
    public static final Object AN_OBJECT_WITH_SILENTLY_IGNORED_ANNOTATIONS = new Object() {
        @Eg(returns = "1")  // Silently ignored
        @Override
        public int hashCode() {
            return 1;
        }
    };
}
```



## Getting Started

See [this gist](https://gist.github.com/dleppik/260d978bf4dcb023bb3218c051653a6c) for an example build.gradle which 
pulls the code you need from Maven Central, or fork 
[this full example project](https://github.com/sheltah22/EgTestExample).

There's also a [Kotlin example project](https://github.com/sheltah22/sharks-and-fishes) using EgTest with Kapt, the
Kotlin annotation processor.

To try it out from source, clone this project and run `./gradlew ':junit-example:build'` from the EgTest directory. 
(Windows: `gradle.bat` should work.) 
Generated source code will be in `junit-example/build/generated/egTest` while JUnit test results will be at 
`junit-example/build/reports/tests/test/index.html`.

Running the Java compiler from the command line isn't recommended, but can be helpful for debugging. 
To process EgTest annotations into JUnit test code, download `egtest-processor-`VERSION`.jar` and compile your source 
with 
`-processorpath [path/to/jar/]egtest-processor-VERSION.jar -Aegtest.targetDirectory=/path/for/generated/source/files`. 

Similarly, running with Maven should work if you configure `compiler:compile` with EgTest in the `annotationProcessors`
parameter, include `-Aegtest.targetDirectory=/path/for/generated/source/files` in `compilerArgs`, and make sure the
unit test compile includes the generated source files. EgTest is unlike most annotation processors in that **its
generated source is compiled with unit tests, not with the main source code.** Thus, you can't just set 
`generatedSourcesDirectory` and be done.