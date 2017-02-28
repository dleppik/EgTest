package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.Example;
import com.vocalabs.egtest.processor.data.FunctionMatchExample;
import com.vocalabs.egtest.processor.data.PatternMatchExample;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Build JUnit test classes
 */
public class JUnitClassWriter {

    public static JavaFile createFileSpec(String classUnderTestName,
                                          MessageHandler messageHandler,
                                          List<Example<?>> items) throws Exception {
        return new JUnitClassWriter(classUnderTestName, messageHandler, items)
                .createFileSpec();
    }

    private final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.bestGuess("org.junit.Test")).build();

    private final MessageHandler messageHandler;
    private final String classUnderTestName;
    private final List<Example<?>> items;
    private final TypeElement classElement;
    private final String className;


    private JUnitClassWriter(String name, MessageHandler messageHandler, List<Example<?>> items) {
        this.classUnderTestName = name;
        this.messageHandler = messageHandler;
        this.items = items;
        if (items.isEmpty())
            throw new IllegalArgumentException("Must have at least one Example to write a test file for "+name);
        this.classElement = JavaModelUtil.topLevelClass(items.get(0).element());
        this.className = classElement.getSimpleName() +"$EgTest";
    }

    private JavaFile createFileSpec() throws Exception {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        addPatternMatchTests(items, typeSpecBuilder);
        addFunctionMatchTests(items, typeSpecBuilder);

        TypeSpec javaFileSpec = typeSpecBuilder.build();

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null) {
            messageHandler.unsupported(classElement, "EgTest does not support classes without packages.");
        }
        String packageName = packageElement.getQualifiedName().toString();
        return JavaFile.builder(packageName, javaFileSpec).build();
    }

    private void addPatternMatchTests(List<Example<?>> examples, TypeSpec.Builder toAddTo) {
        final Map<Element, List<PatternMatchExample>> byElement = examples.stream()
                .filter(it -> it instanceof PatternMatchExample)
                .map(it -> (PatternMatchExample) it)
                .collect(Collectors.groupingBy(PatternMatchExample::element));
        addPatternMatchTests(toAddTo, byElement);
    }

    private void addPatternMatchTests(TypeSpec.Builder toAddTo, Map<Element, List<PatternMatchExample>> byElement) {
        for (Map.Entry<Element, List<PatternMatchExample>> entry: byElement.entrySet()) {
            Element element = entry.getKey();

            if ( ! checkSupport(element))
                continue;

            String methodName = "testMatch$"+element.getSimpleName();

            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .addException(Exception.class)
                    .returns(void.class);

            for (PatternMatchExample example: entry.getValue()) {
                ClassName assertion = booleanAssertion(example.annotation());
                String description = example.annotation().annotationType().getSimpleName()+" "+example.toMatch();
                ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
                if (element.getModifiers().contains(Modifier.STATIC)) {
                    String patternName = element.getSimpleName().toString();
                    specBuilder.addCode(
                            "$L($S, $T.$L.matcher($S).matches());\n",
                            assertion, description, className, patternName, example.toMatch());
                }
                else {
                    messageHandler.note("Creating case for non-static "
                            +element.getSimpleName()
                            +" in "+classUnderTestName+" with "+methodName); // XXX
                    String patternName = element.getSimpleName().toString();
                    specBuilder.addCode(
                            "$L($S, new $T().$L.matcher($S).matches());\n",
                            assertion, description, className, patternName, example.toMatch());
                }
            }
            messageHandler.note("Creating method "+methodName+" for "+classUnderTestName); // XXX
            toAddTo.addMethod(specBuilder.build());
        }
    }

    private void addFunctionMatchTests(List<Example<?>> examples, TypeSpec.Builder toAddTo) {
        final Map<Element, List<FunctionMatchExample>> byElement = examples.stream()
                .filter(it -> it instanceof FunctionMatchExample)
                .map(it -> (FunctionMatchExample) it)
                .collect(Collectors.groupingBy(FunctionMatchExample::element));
        addFunctionMatchTests(toAddTo, byElement);
    }

    private void addFunctionMatchTests(TypeSpec.Builder toAddTo, Map<Element, List<FunctionMatchExample>> byElement) {
        for (Map.Entry<Element, List<FunctionMatchExample>> entry: byElement.entrySet()) {
            Element element = entry.getKey();
            if ( ! checkSupport(element)) {
                continue;
            }
            String methodName = "testMatch$"+element.getSimpleName();
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .returns(void.class);

            for (FunctionMatchExample example: entry.getValue()) {
                ClassName assertion = booleanAssertion(example.annotation());
                String description = example.annotation().annotationType().getSimpleName()+" "+example.toMatch();
                ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
                String patternName = element.getSimpleName().toString();
                if (element.getModifiers().contains(Modifier.STATIC)) {
                    specBuilder.addCode(
                            "$L($S, $T.$L($S));\n",
                            assertion, description, className, patternName, example.toMatch());
                }
                else {
                    specBuilder.addCode(
                            "$L($S, new $T().$L($S));\n",
                            assertion, description, className, patternName, example.toMatch());
                }
            }

            toAddTo.addMethod(specBuilder.build());
        }
    }


    /** If the element is unsupported, return false after writing to messageHandler. */
    private boolean checkSupport(Element element) {
        if (! element.getEnclosingElement().equals(this.classElement)) {
            messageHandler.unsupported(element, "inner classes");
            return false;
        }

        if (! visible(element) ) {
            messageHandler.unsupported(element, "non-public");
            return false;
        }
        return true;
    }

    private boolean visible(Element el) {
        Set<Modifier> modifiers = el.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.DEFAULT);
    }

    private ClassName booleanAssertion(Annotation annotation) {
        final String boolStr;
        if (annotation instanceof EgMatches)
            boolStr = "True";
        else if (annotation instanceof EgNoMatch)
            boolStr = "False";
        else throw new IllegalArgumentException(this+" doesn't handle "+annotation);
        return ClassName.get("org.junit.Assert", "assert"+boolStr);
    }
}
