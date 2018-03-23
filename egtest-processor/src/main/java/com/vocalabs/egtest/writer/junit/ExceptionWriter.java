package com.vocalabs.egtest.writer.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.processor.data.ExceptionExample;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

class ExceptionWriter extends TestWriter<ExecutableElement, ExceptionExample> {

    private static final ClassName ASSERT_TRUE = ClassName.get("org.junit.Assert", "assertTrue");
    private static final ClassName ASSERT_FAIL = ClassName.get("org.junit.Assert", "fail");


    public ExceptionWriter(ExecutableElement element, List<ExceptionExample> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        super(element, examples, classWriter, toAddTo);
    }

    @NotNull
    @Override
    protected String baseName() {
        return "Exception";
    }

    @Override
    public void addTests() {
        if (notSupported())
            return;

        String newMethodName = testMethodName();
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(newMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getTestAnnotation())
                .addException(Exception.class)
                .returns(void.class);

        ClassName className = ClassName.get((TypeElement) getElement().getEnclosingElement());
        String methodName = getElement().getSimpleName().toString();

        for (ExceptionExample example: getExamples()) {
            String arguments = String.join(", ", example.getAnnotation().value());
            TypeName exceptionType = example.exceptionType();
            String description = getElement().getSimpleName()+"("+arguments+") should throw "+exceptionType;
            specBuilder.addCode(
                    "try {\n");
            specBuilder.addCode("    "+methodCall(className, methodName, example)+"\n");
            specBuilder.addCode(
                    "    $L($S);\n"+
                    "} catch (Throwable ex) {\n"+
                    "    if (ex instanceof java.lang.AssertionError)\n"+
                    "        throw ex;\n"+
                    "    $L($S+\", instead threw \"+ex, ex instanceof $L);\n"+
                    "}\n",
                    ASSERT_FAIL, description+", returned without error",
                    ASSERT_TRUE, description, exceptionType);
        }
        getToAddTo().addMethod(specBuilder.build());
    }

    private CodeBlock methodCall(ClassName className, String methodName, ExceptionExample example) {
        String arguments = String.join(", ", example.getAnnotation().value());

        if (getElement().getModifiers().contains(Modifier.STATIC)) {
            return CodeBlock.builder()
                    .add("$T.$L($L);", className, methodName, arguments)
                    .build();
        }
            String constructorArgs = String.join(", ", example.getAnnotation().construct());
            return CodeBlock.builder()
                    .add("new $T($L).$L($L);", className, constructorArgs, methodName, arguments)
                    .build();
    }
}
