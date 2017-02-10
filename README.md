# EGTest
Annotations for simple assertions in Java and other JVM languages

###### Definition
e.g., _exempli gratia:_ for the sake of an example.

Not to be confused with i.e., _id et_: it is. (A definition or exhaustive list.)

##What it's for (and not for)
EGTest annotations provide **testable documentation in the source code**.

EGTest annotations automatically show up in Javadoc, so you can have confidence that the examples work as documented.

By removing the excuses for writing tests, you may find yourself writing tests for one-off code or exploratory code. 
Rather than testing in a REPL, it is easier to write a permanent unit test. Even simple one-line functions are worth a 
unit test when it's this quick and easy!

EGTest is **not** an excuse to avoid writing complete unit tests! Or test suites in general. In some cases, especially
with functional programming, you might be able to avoid writing a test file. That said, EGTest should encourage you to 
break your complex code into small, simple, easily tested functions. With EGTest, you don't need to write several lines
of unit test boilerplate for a one-line test-- but you still need that boilerplate for more complicated tests.

EGTest provides the annotations, not the code generators for the unit tests. The annotations are independent of any
particular test or build tool.
**INSERT LINK TO GRADLE/JUNIT EGTEST PROJECT, WHEN IT EXISTS.**

##The Annotations

There are only three annotations for describing test cases:

`@Eg` takes a list of examples of things that work. For regular expressions, these are Strings which match. For a 
boolean method with a single argument, the example causes it to return true. For more complex methods, the example may 
be a String containing the arguments and expected result.

`@EgNot` takes a list of examples which **don't** work. Regular expressions don't match, boolean methods return false, 
etc.

`@EgThrow` takes a list of examples which throw an exception.

##Some examples

```Java
import java.util.regex.Pattern;
public class Example {

    /**
     * Regular expression to match address portions of typical real-world email addresses.
     * It does NOT attempt to match all valid RFC 2822 addresses.
     */
     @Eg("dleppik@vocalabs.com", "dleppik@vocalabs.example.com")
     @EgNot("dleppik", "dleppik@vocalabs@example.com", "David Leppik <dleppik@vocalabs.com>")
    public static final Pattern SIMPLE_EMAIL = Pattern.compile("...");
    
    @Eg("(1,1) -> 2", "(Integer.MAX_VALUE, 1) -> Integer.MIN_VALUE")
    public static int add(int a, int b) {
        return a + b;
    }
    
    @EgThrow("null")
        public static String methodWhichCannotHandleNulls(Object thing1) {
            return thing1.toString();
        }
    
    @EgThrow("(null, \"Hello\")", "(\"Hello\", null)")
    public static String anotherMethodWhichCannotHandleNulls(Object thing1, Object thing2) {
        return thing1.toString() + thing2.toString();
    }
    
    // When testing a non-static method, the default constructor is used
    private int decrementStep = 1;
    
    @eg("(1) -> 0", "(Integer.MIN_VALUE) -> Integer.MAX_VALUE")
    public int decrement(int a, int b) {
        return a - decrementStep;
    }
}
```
