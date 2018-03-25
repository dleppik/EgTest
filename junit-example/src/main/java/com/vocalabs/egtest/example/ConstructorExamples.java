package com.vocalabs.egtest.example;


import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgException;
import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.util.regex.Pattern;

public class ConstructorExamples {
    private static final String DEFAULT_COMMENT = "No comment";

    private final int intValue;
    private final float floatValue;
    private final String comment;

    @EgMatch("No comment")
    @EgMatch(value = "Boo", construct = {"10", "3f", "\"Boo\""})
    @EgNoMatch(value = "")
    @EgNoMatch(value = "", construct = {"7.8f"})
    public final Pattern commentPattern;

    public ConstructorExamples(int intValue, float floatValue, String comment) {
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.comment = comment;
        this.commentPattern = Pattern.compile(Pattern.quote(comment));
    }

    public ConstructorExamples(int intValue) {
        this(intValue, intValue, DEFAULT_COMMENT);
    }

    public ConstructorExamples(float floatValue) {
        this(Math.round(floatValue), floatValue, DEFAULT_COMMENT);
    }

    public ConstructorExamples() {
        this(5, 5, DEFAULT_COMMENT);
    }


    @Eg(given = "5", returns = "10")
    @Eg(construct = "6",
            given = "5", returns = "11")
    @Eg(construct = "5.8f",
            given = "5", returns = "11")
    @Eg(construct = {"5", "5.8f", "\"Hello\""},
            given = "5", returns = "10")
    public int addInt(int i) {
        return i + this.intValue;
    }

    @EgMatch("ConstructorExamples{intValue=5, floatValue=5.0, comment='No comment'}")
    @EgMatch(construct = {"10", "3f", "\"Boo\""},
             value = "ConstructorExamples{intValue=10, floatValue=3.0, comment='Boo'}")
    @EgMatch(construct = {"7"},
             value = "ConstructorExamples{intValue=7, floatValue=7.0, comment='No comment'}")
    @EgMatch(construct = {"7.8f"},
             value = "ConstructorExamples{intValue=8, floatValue=7.8, comment='No comment'}")
    @EgNoMatch(construct = {"7.8f"},
               value = "ConstructorExamples{intValue=7, floatValue=7.8, comment='No comment'}")
    public boolean sameAsToString(String s) {
        return toString().equals(s);
    }

    @Eg(returns = "\"NO COMMENT\"")
    @EgException(value = {}, construct = {"5", "5", "null"})
    public String uppercaseComment() {
        return comment.toUpperCase();
    }

    public String toString() {
        return "ConstructorExamples{" +
                "intValue=" + intValue +
                ", floatValue=" + floatValue +
                ", comment='" + comment + '\'' +
                '}';
    }
}
