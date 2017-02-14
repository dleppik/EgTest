# EgTest
Annotations for simple assertions in Java and other JVM languages

###### Definition
e.g., _exempli gratia:_ for the sake of an example.

Not to be confused with i.e., _id et_: it is. (A definition or exhaustive list.)

## What it's for (and not for)
EgTest annotations provide **testable documentation in the source code**.

EgTest annotations automatically show up in Javadoc, so you can have confidence that the examples work as documented.

By removing the excuses for writing tests, you may find yourself writing tests for one-off code or exploratory code. 
Rather than testing in a REPL, it is easier to write a permanent unit test. Even simple one-line functions are worth a 
unit test when it's this quick and easy!

EgTest is **not** an excuse to avoid writing complete unit tests! In some cases, especially
with functional programming, you might be able to avoid writing a test file. EgTest should encourage you to 
break your complex code into small, simple, easily tested functions. With EgTest, you don't need to write several lines
of unit test boilerplate for a one-line test-- but you still need that boilerplate for more complicated tests.

Because EgTest encourages you to think small, **private methods and regular expressions are fair game.** If you break a 
big method into several small private pieces, you can test each private piece individually.

EgTest provides the annotations, not the code generators for the unit tests. The annotations are independent of any
particular test or build tool.
**INSERT LINK TO GRADLE/JUNIT EGTEST PROJECT, WHEN IT EXISTS.**

### Examples

See [The example source code](https://github.com/dleppik/EgTest/tree/master/src/main/java/com/vocalabs/egtest/example).

```Java
import java.util.regex.Pattern;
import com.vocalabs.egtest.annotation.*;
public class ExampleForReadme {

    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
    @EgMatches("dleppik@vocalabs.com")
    @EgMatches("dleppik@vocalabs.example.com")
    
    @EgNoMatch("dleppik")
    @EgNoMatch("dleppik@vocalabs@example.com")
    @EgNoMatch("David Leppik <dleppik@vocalabs.com>")
    public static final Pattern SIMPLE_EMAIL = Pattern.compile("...");
    
    @Eg(given = {"1", "2"}, returns = "3")
    @Eg(given = {"1", "Integer.MIN_VALUE"}, returns = "Integer.MAX_VALUE")
    public static int add(int a, int b) {
        return a + b;
    }
    
    @EgException(value = {"null"}, willThrow = NullPointerException.class)
    public static String methodWhichCannotHandleNulls(Object thing1) {
        return thing1.toString();
    }
    
    @EgException({"null", "hello"})
    @EgException({"hello", "null"})
    public static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }
}
```


##Generating Unit Tests

A properly formed EgTest unit test is in the same package as the class being tested, but it should have a name which
does not conflict with typical unit test class names. That way the same test suite can simultaneously run EgTests and 
hand-written tests. For example, a class `org.example.MyExample.class` might yield a test class named 
`org.example.MyExample$EgTest.class`.

Method parameters and return types should be constants, but they can be of any type import from anywhere, so long as 
it is visible to the test. Thus `java.time.Instant.EPOCH`, with the full package name, may be used.

##Open questions

* We have a few proposed syntaxes; it's not clear which one to go with.