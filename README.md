# EGTest
Annotations for simple assertions in Java and other JVM languages

###### Definition
e.g., _exempli gratia:_ for the sake of an example.

Not to be confused with i.e., _id et_: it is. (A definition or exhaustive list.)

## What it's for (and not for)
EGTest annotations provide **testable documentation in the source code**.

EGTest annotations automatically show up in Javadoc, so you can have confidence that the examples work as documented.

By removing the excuses for writing tests, you may find yourself writing tests for one-off code or exploratory code. 
Rather than testing in a REPL, it is easier to write a permanent unit test. Even simple one-line functions are worth a 
unit test when it's this quick and easy!

EGTest is **not** an excuse to avoid writing complete unit tests! In some cases, especially
with functional programming, you might be able to avoid writing a test file. EGTest should encourage you to 
break your complex code into small, simple, easily tested functions. With EGTest, you don't need to write several lines
of unit test boilerplate for a one-line test-- but you still need that boilerplate for more complicated tests.

Because EGTest encourages you to think small, **private methods and regular expressions are fair game.** If you break a 
big method into several small private pieces, you can test each private piece individually.

EGTest provides the annotations, not the code generators for the unit tests. The annotations are independent of any
particular test or build tool.
**INSERT LINK TO GRADLE/JUNIT EGTEST PROJECT, WHEN IT EXISTS.**

## The Annotations

Right now we are exploring the design tradeoffs by using two possible syntaxes. Style 1 is more verbose and type-safe
and requires Java 8's repeatable annotations. Style 2 has a more compact syntax.

Because Style 1 is easier to implement, it is what we are focusing our tooling efforts on. Both styles are a work in
progress.

### Examples (proposed style 1)

**NOTE: This is a work in progress; see the source code for current thinking.**

### Examples (proposed style 2)

**This is a work in progress; see the source code for current thinking.**


```Java
import java.util.regex.Pattern;
public class Example {

    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @Eg({"dleppik@vocalabs.com", "dleppik@vocalabs.example.com"})
    @EgNot({"dleppik", "dleppik@vocalabs@example.com", "David Leppik <dleppik@vocalabs.com>"})
    public static final Pattern SIMPLE_EMAIL = Pattern.compile("...");
    
    @Eg({"(1,1) -> 2", "(Integer.MAX_VALUE, 1) -> Integer.MIN_VALUE"})
    public static int add(int a, int b) {
        return a + b;
    }
    
    @EgThrow({"null"})
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }
    
    @EgThrow({"(null, \"Hello\")", "(\"Hello\", null)"})
    public static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }
    
    // When testing a non-static method, the default constructor is used
    private int decrementStep = 1;
    
    @Eg({"(1) -> 0", "(Integer.MIN_VALUE) -> Integer.MAX_VALUE"})
    public int decrement(int a, int b) {
        return a - decrementStep;
    }
}
```

##Generating Unit Tests

A properly formed EgTest unit test is in the same package as the class being tested, but it should have "EgTest"
in its name as a minimal attempt to not conflict with typical unit test class names. That way the same test suite can 
simultaneously run EgTests and hand-written tests. For example, a class 
`org.example.MyExample.class` might yield a test class named `org.example.MyExampleEgTest.class`.

Method parameters and return types should be constants, but they can be of any type import from anywhere, so long as 
it is visible to the test. Thus `java.time.Instant.EPOCH`, with the full package name, may be used.

##Open questions

* We have a few proposed syntaxes; it's not clear which one to go with.