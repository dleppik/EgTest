package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.data.FunctionMatchExample;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

class FunctionMatchWriter extends MatchWriter<Element, FunctionMatchExample> {

    FunctionMatchWriter(Element e, List<FunctionMatchExample> exs, ClassWriter w, TypeSpec.Builder b) {
        super(e, exs, w, b);
    }

    @Override
    public void addTests() {
        if (notSupported())
            return;

        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(testMethodName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(testAnnotation)
                .returns(void.class);

        for (FunctionMatchExample example: examples) {
            ClassName assertion = booleanAssertion(example.getAnnotation());
            String description = example.getAnnotation().annotationType().getSimpleName()+" "+example.toMatch();
            ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
            String methodName = element.getSimpleName().toString();
            if (element.getModifiers().contains(Modifier.STATIC)) {
                specBuilder.addCode(
                        "$L($S, $T.$L($S));\n",
                        assertion, description, className, methodName, example.toMatch());
            }
            else {
                String constructorArgs = example.constructorArgs().stream().reduce((a,b) -> a+", "+b).orElse("");
                specBuilder.addCode(
                        "$L($S, new $T($L).$L($S));\n",
                        assertion, description, className, constructorArgs, methodName, example.toMatch());
            }
        }
        toAddTo.addMethod(specBuilder.build());
    }
}
