package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.math.NumberUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/** Wraps Eg code in a Groovy script interpreter, unless it can trivially prove that it isn't necessary. */
public class GroovyInjector implements LanguageInjector {

    private static final String EXEC_NAME = "execGroovy";

    private boolean needsClassDecoration = false;
    private final String packageName;

    public GroovyInjector(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void add(MethodSpec.Builder specBuilder, TypeMirror type, String egText) {
        if (plainJava(type, egText))
            simpleAdd(specBuilder, egText);
        else
            scriptAdd(specBuilder, type, egText);
    }

    private void simpleAdd(MethodSpec.Builder specBuilder, String egText) {
        specBuilder.addCode("$L", egText);
    }

    private void scriptAdd(MethodSpec.Builder specBuilder, TypeMirror type, String egText) {
        needsClassDecoration = true;
        specBuilder.addCode("($T) $L($S)", type, EXEC_NAME, egText);
    }

    @Override
    public void decorateClass(TypeSpec.Builder toAddTo) {
        if (needsClassDecoration) {
            ParameterSpec param = ParameterSpec.builder(String.class, "code").build();
            ClassName shell = ClassName.get("groovy.lang", "GroovyShell");
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(EXEC_NAME)
                    .addModifiers(Modifier.PRIVATE)
                    .addParameter(param)
                    .returns(Object.class)
                    .addCode("StringBuilder script = new StringBuilder();\n")
                    .addCode("script.append($S);\n", "import "+packageName+".*\n")
                    .addCode("script.append($N);\n", param)
                    .addCode("return new $L().evaluate(script.toString());\n", shell);
            toAddTo.addMethod(specBuilder.build());
        }
    }

    /** Return true if the text doesn't need Groovy. */
    private boolean plainJava(TypeMirror tm, String text) {
        return plainJava(tm.getKind(), text);
    }

    /** Exposed for unit testing. */
    static boolean plainJava(TypeKind kind, String text) {
        switch (kind) {
            case BOOLEAN: return "true".equals(text) || "false".equals(text);
            case BYTE:    return NumberUtils.isCreatable(text);  // Unfortunately doesn't handle "12_345".
            case CHAR:    return text.matches("'.'");            // e.g. 'a', '\t'; note that newline isn't a character
            case DOUBLE:  return NumberUtils.isCreatable(text);
            case FLOAT:   return NumberUtils.isCreatable(text);
            case INT:     return NumberUtils.isCreatable(text);
            case LONG:    return NumberUtils.isCreatable(text);
            case SHORT:   return NumberUtils.isCreatable(text);
            default:      return false;
        }
    }
}
