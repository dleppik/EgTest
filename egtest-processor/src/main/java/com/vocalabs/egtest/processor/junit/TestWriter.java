package com.vocalabs.egtest.processor.junit;


import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.*;

import javax.lang.model.element.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class TestWriter<T extends Element, X extends EgItem<?>> {
    protected final ClassWriter classWriter;
    protected final List<X> examples;
    protected final TypeSpec.Builder toAddTo;
    protected final MessageHandler messageHandler;
    protected final T element;
    protected final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.get("org.junit", "Test")).build();

    static void write(ClassWriter classWriter, TypeSpec.Builder builder) {
        Map<Element, List<EgItem<?>>> examplesByElement = classWriter.getItems().stream()
                .collect(Collectors.groupingBy(EgItem::getElement));

        for (Map.Entry<Element, List<EgItem<?>>> entry: examplesByElement.entrySet()) {
            Element el = entry.getKey();
            List<EgItem<?>> examples = entry.getValue();

            new PatternMatchWriter(el, filterAndConvert(PatternMatchExample.class, examples), classWriter, builder)
                    .addTests();
            new FunctionMatchWriter(el, filterAndConvert(FunctionMatchExample.class, examples), classWriter, builder)
                    .addTests();

            if (el instanceof ExecutableElement) {
                ExecutableElement exEl = (ExecutableElement) el;

                List<ReturnsExample> returnsExamples = filterAndConvert(ReturnsExample.class, examples);
                new EgWriter(exEl, returnsExamples, classWriter, builder)
                        .addTests();

                List<ExceptionExample> exceptionExamples = filterAndConvert(ExceptionExample.class, examples);
                new ExceptionWriter(exEl, exceptionExamples, classWriter, builder)
                        .addTests();
            }
        }
    }

    private static <X extends EgItem<?>> List<X> filterAndConvert(Class<X> cl, List<?> items) {
        return items.stream()
                .filter(it -> cl.isAssignableFrom(it.getClass()))
                .map(cl::cast)
                .collect(Collectors.toList());
    }

    protected TestWriter(T element, List<X> examples, ClassWriter classWriter, TypeSpec.Builder toAddTo) {
        this.element = element;
        this.examples = examples;
        this.classWriter = classWriter;
        this.toAddTo = toAddTo;
        this.messageHandler = classWriter.getMessageHandler();
    }

    public abstract void addTests();

    /**
     * Return false if work needs to be done on this element.
     * If the element is unsupported, return true after alerting messageHandler.
     */
    protected boolean notSupported() {
        if (examples.isEmpty())
            return true;

        if (inInnerClass()
                && ! element.getModifiers().contains(Modifier.STATIC)
                && ! enclosingClass().getModifiers().contains(Modifier.STATIC)) {
            messageHandler.unsupported(element, "non-static inner class");
            return true;
        }

        if (! visible()) {
            messageHandler.unsupported(element, "private or protected");
            return true;
        }
        return false;
    }

    private boolean visible() {
        Set<Modifier> modifiers = element.getModifiers();
        return ! modifiers.contains(Modifier.PRIVATE)  &&  ! modifiers.contains(Modifier.PROTECTED);
    }

    private TypeElement enclosingClass() {
        Element el = element;
        while ( ! (el instanceof TypeElement) ) {
            el = el.getEnclosingElement();
        }
        return (TypeElement) el;
    }

    private boolean inInnerClass() {
        return ! element.getEnclosingElement().equals(classWriter.getClassElement());
    }

    private String innerClassNamePortion() {
        TypeElement outerEl = classWriter.getClassElement();
        StringBuilder sb = new StringBuilder();
        for (Element el = element.getEnclosingElement(); ! el.equals(outerEl); el = el.getEnclosingElement()) {
            sb.append("$");
            sb.append(uniqueName(el));
        }
        return sb.toString();
    }

    protected String testMethodName() {
        return "test"+baseName()+innerClassNamePortion()+"$"+uniqueName(element);
    }

    private static String uniqueName(Element el) {
        String simpleName = el.getSimpleName().toString();
        if (nameCollisions(el).isEmpty())
            return simpleName;
        return elementUniqueSuffix(el);
    }

    private static List<Element> nameCollisions(Element el) {
        return el.getEnclosingElement()
                .getEnclosedElements().stream()
                .filter(it -> ! it.equals(el))
                .filter(it -> it.getSimpleName().toString().equals(el.getSimpleName().toString()))
                .map(it -> (Element) it)         // Some Java compilers don't like <? extends Element>
                .collect(Collectors.toList());
    }

    private static String elementUniqueSuffix(Element el) {
        if (el instanceof ExecutableElement) {
            Stream<String> params = ((ExecutableElement)el).getParameters()
                    .stream()
                    .map(p -> p.asType().toString());
            return escapeParameterNames(params);
        }
        return stripGenerics("$"+el.getKind());
    }

    /**
     * Escape names for use in generating unique JUnit names. See the unit tests for expected results of the current
     * implementation; it may change over time.
     */
    static String escapeParameterNames(Stream<String> names) {
        return names
                .map(TestWriter::stripGenerics)
                .map(s -> s.replace("$", "$$"))
                .map(s -> s.replace("_", "__"))
                .map(s -> s.replace(".", "_"))
                .map(s -> s.replace("[]", "$_A"))
                .reduce("",  (a,b)  -> a+"$"+b);
    }

    private static String stripGenerics(String s) { return s.replaceAll("<.*?>", ""); }

    /** The distinctive portion of the name constructed by {@link #testMethodName()}.  */
    protected abstract String baseName();
}
