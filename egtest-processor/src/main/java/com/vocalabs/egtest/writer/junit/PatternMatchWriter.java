package com.vocalabs.egtest.writer.junit;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.data.PatternMatchExample;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

class PatternMatchWriter extends MatchWriter<Element, PatternMatchExample> {

    PatternMatchWriter(Element e, List<PatternMatchExample> exs, JUnitClassWriter w, TypeSpec.Builder b) {
        super(e, exs, w, b);
    }

    @Override
    public void addTests() {
        if (notSupported())
            return;

        String methodName = testMethodName();

        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(getTestAnnotation())
                .addException(Exception.class)
                .returns(void.class);

        for (PatternMatchExample example : getExamples()) {
            ClassName assertion = booleanAssertion(example.getAnnotation());
            String description = example.getAnnotation().annotationType().getSimpleName() + " " + example.toMatch();
            ClassName className = ClassName.get((TypeElement) getElement().getEnclosingElement());
            String patternName = getElement().getSimpleName().toString();
            if (getElement().getModifiers().contains(Modifier.STATIC)) {
                specBuilder.addCode(
                        "$L($S, $T.$L.matcher($S).matches());\n",
                        assertion, description, className, patternName, example.toMatch());
            } else {
                specBuilder.addCode(
                        "$L($S, new $T().$L.matcher($S).matches());\n",
                        assertion, description, className, patternName, example.toMatch());
            }
        }
        getToAddTo().addMethod(specBuilder.build());
    }
}
