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

abstract class JUnitExampleWriter<T extends Element, X extends Example<?>> {
    protected final JUnitClassWriter classWriter;
    protected final List<X> examples;
    protected final TypeSpec.Builder toAddTo;
    protected final MessageHandler messageHandler;
    protected final T element;
    protected final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.get("org.junit", "Test")).build();

    static void write(JUnitClassWriter classWriter, TypeSpec.Builder builder) {
        Map<Element, List<Example<?>>> examplesByElement = classWriter.getItems().stream()
                .collect(Collectors.groupingBy(Example::getElement));

        for (Map.Entry<Element, List<Example<?>>> entry: examplesByElement.entrySet()) {
            Element el = entry.getKey();
            List<Example<?>> examples = entry.getValue();

            new PatternMatchWriter(el, filterAndConvert(PatternMatchExample.class, examples), classWriter, builder)
                    .addTests();
            new FunctionMatchWriter(el, filterAndConvert(FunctionMatchExample.class, examples), classWriter, builder)
                    .addTests();

            if (el instanceof ExecutableElement) {
                ExecutableElement exEl = (ExecutableElement) el;

                List<ReturnsExample> returnsExamples = filterAndConvert(ReturnsExample.class, examples);
                new ReturnsWriter(exEl, returnsExamples, classWriter, builder)
                        .addTests();

                List<ExceptionExample> exceptionExamples = filterAndConvert(ExceptionExample.class, examples);
                new ExceptionWriter(exEl, exceptionExamples, classWriter, builder)
                        .addTests();
            }
        }
    }

    private static <X extends Example<?>> List<X> filterAndConvert(Class<X> cl, List<?> items) {
        return items.stream()
                .filter(it -> cl.isAssignableFrom(it.getClass()))
                .map(cl::cast)
                .collect(Collectors.toList());
    }

    protected JUnitExampleWriter(T element, List<X> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        this.element = element;
        this.examples = examples;
        this.classWriter = classWriter;
        this.toAddTo = toAddTo;
        this.messageHandler = classWriter.getMessageHandler();
    }

    public abstract void addTests();

    /** If the element is unsupported, return false after writing to messageHandler. */
    protected boolean checkSupport() {
        if (examples.isEmpty())
            return false;

        if (! element.getEnclosingElement().equals(classWriter.getClassElement())) {
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

    protected String testMethodName() {
        return "test"+baseName()+"$"+uniqueName();
    }

    private String uniqueName() {
        String simpleName = element.getSimpleName().toString();
        if (nameCollisions().isEmpty())
            return simpleName;
        return elementUniqueSuffix(element);
    }

    private List<Element> nameCollisions() {
        return element.getEnclosingElement()
                .getEnclosedElements().stream()
                .filter(it -> ! it.equals(element))
                .filter(it -> it.getSimpleName().toString().equals(element.getSimpleName().toString()))
                .map(it -> (Element) it)         // Some Java compilers don't like <? extends Element>
                .collect(Collectors.toList());
    }

    private String elementUniqueSuffix(Element el) {
        if (el instanceof ExecutableElement) {
            //
            // Given   foo(String, double, Double[], org.omg.CORBA_2_3.ORB)
            // returns "$java_lang_String$double$java_lang_Double$_A$org_omg_CORBA__2__3_ORB"
            //
            // I was tempted to use ᛜ (runic ingwaz) but I'm sure people would complain
            return ((ExecutableElement)el).getParameters()
                    .stream()
                    .map(p -> p.asType().toString())
                    .map(s -> s.replaceAll("_", "__"))
                    .map(s -> s.replaceAll("\\.", "_"))
                    .map(s -> s.replaceAll("\\[\\]", "$_A"))
                    .reduce("",  (a,b)  -> a+"$"+b);
        }
        return "$"+el.getKind();
    }


    /** The distinctive portion of the name constructed by {@link #testMethodName()}.  */
    protected abstract String baseName();
}
