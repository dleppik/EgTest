package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.data.ReturnsExample;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

class ReturnsWriter extends JUnitExampleWriter<ExecutableElement, ReturnsExample> {
    public ReturnsWriter(ExecutableElement element, List<ReturnsExample> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        super(element, examples, classWriter, toAddTo);
    }

    @Override
    protected String baseName() {
        return "Returns";
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

        ClassName assertion = ClassName.get("org.junit.Assert", "assertEquals");
        ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
        for (ReturnsExample example: examples) {
            String arguments = String.join(", ", example.getAnnotation().given());
            String expected  = example.getAnnotation().returns();
            String description = element.getSimpleName()+"("+arguments+")";
            String methodName = element.getSimpleName().toString();

            if (element.getModifiers().contains(Modifier.STATIC)) {
                specBuilder.addCode(
                        "$L($S, $L, $T.$L($L));\n",
                        assertion, description, expected, className, methodName, arguments);
            }
            else {
                specBuilder.addCode(
                        "$L($S, $L, new $T().$L($L));\n",
                        assertion, description, expected, className, methodName, arguments);
            }
        }
        toAddTo.addMethod(specBuilder.build());
    }
}
