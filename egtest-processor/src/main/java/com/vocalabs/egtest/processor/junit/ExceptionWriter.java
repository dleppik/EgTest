package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.processor.data.ExceptionExample;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

class ExceptionWriter extends JUnitExampleWriter<ExecutableElement, ExceptionExample> {

    private static final ClassName ASSERT_TRUE = ClassName.get("org.junit.Assert", "assertTrue");
    private static final ClassName ASSERT_FAIL = ClassName.get("org.junit.Assert", "fail");


    public ExceptionWriter(ExecutableElement element, List<ExceptionExample> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        super(element, examples, classWriter, toAddTo);
    }

    @Override
    protected String baseName() {
        return "Exception";
    }

    @Override
    public void addTests() {
        if ( ! checkSupport())
            return;

        String newMethodName = testMethodName();
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(newMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(testAnnotation)
                .addException(Exception.class)
                .returns(void.class);

        ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
        String methodName = element.getSimpleName().toString();

        for (ExceptionExample example: examples) {
            String arguments = String.join(", ", example.getAnnotation().value());
            TypeName exceptionType = example.exceptionType();
            String description = element.getSimpleName()+"("+arguments+") should throw "+exceptionType;
            String callTemplate = element.getModifiers().contains(Modifier.STATIC)
                    ? "$T.$L($L);"
                    : "new $T().$L($L);";
            specBuilder.addCode(
                    "try {\n"+
                    "    "+callTemplate+"\n"+
                    "    $L($S);\n"+
                    "} catch (Throwable ex) {\n"+
                    "    if (ex instanceof java.lang.AssertionError)\n"+
                    "        throw ex;\n"+
                    "    $L($S+\", instead threw \"+ex, ex instanceof $L);\n"+
                    "}\n",
                    className, methodName, arguments,
                    ASSERT_FAIL, description+", returned without error",
                    ASSERT_TRUE, description, exceptionType);
        }
        toAddTo.addMethod(specBuilder.build());
    }
}
