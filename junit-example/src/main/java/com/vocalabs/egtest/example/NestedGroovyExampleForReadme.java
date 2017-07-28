package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgDefaultLanguage;
import com.vocalabs.egtest.annotation.EgLanguage;

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
