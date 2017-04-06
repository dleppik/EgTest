package com.vocalabs.egtest.language;

import java.util.List;
import groovy.lang.GroovyShell;


public class GroovySupport implements LanguageSupport {
    private static GroovySupport instance = new GroovySupport();

    public static GroovySupport getInstance() { return instance; }

    private GroovySupport() { }

    @Override
    public Object eval(List<String> imports, String text) {
        StringBuilder script = new StringBuilder();
        for (String i : imports) {
            script.append("import ")
                    .append(i)
                    .append("\n");
        }
        script.append("\n");
        script.append(text);

        // javax.script.ScriptEngineManager might not see Groovy, even if Groovy is installed;
        // this may just be a problem with not having done a full build, but we won't worry about it
        // until we're ready to support other languages.
        return new GroovyShell().evaluate(script.toString());
    }
}
