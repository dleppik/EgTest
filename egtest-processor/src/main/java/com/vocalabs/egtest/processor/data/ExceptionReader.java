package com.vocalabs.egtest.processor.data;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.vocalabs.egtest.annotation.*;
import com.vocalabs.egtest.processor.MessageHandler;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExceptionReader implements AnnotationReader<EgItem<?>> {

    public static final ExceptionReader INSTANCE = new ExceptionReader();

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() {
        return Stream.of(
                EgException.class,
                EgExceptionContainer.class)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EgItem<?>> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        if ( ! (element instanceof ExecutableElement)) {
            messageHandler.unsupported(element, annotation, "non-executable");
            return Collections.emptyList();
        }
        ExecutableElement el = (ExecutableElement) element;

        //
        // Because we're dealing with exception classes which may not exist in the compiler, we need to go back to the
        // AnnotationMirrors and find the corresponding Annotation. EgException can't coexist with
        // an EgExceptionContainer, so when we find one we've found them all.
        //
        for (AnnotationMirror annotationMirror:  element.getAnnotationMirrors()) {
            String annotationName = annotationMirror.getAnnotationType().asElement().getSimpleName().toString();

            if (annotationName.equals(EgException.class.getSimpleName())) {
                return example((EgException)annotation, el, annotationMirror);
            }

            if (annotationName.equals(EgExceptionContainer.class.getSimpleName())) {
                AnnotationValue egArrayValue = getAnnotationValue(annotationMirror, "value");
                if (egArrayValue == null)
                    return Collections.emptyList();
                List<AnnotationMirror> values = (List<AnnotationMirror>) egArrayValue.getValue();
                return values.stream()
                        .flatMap(am -> example(el, am).stream())
                        .collect(Collectors.toList());
            }
        }
        throw new IllegalArgumentException("Unsupported annotation: "+annotation);
    }

    /** Constructs an AnnotationValue from the AnnotationMirror. */
    private List<EgItem<?>> example(ExecutableElement el, AnnotationMirror am) {
        final String[] parameters = annotationValues(am, "value");
        final String[] constructorArgs = annotationValues(am, "construct");

        EgException egException = new EgException() {
            @Override public String[] construct() { return constructorArgs; }
            @Override public String[] value()     { return parameters;      }

            @Override
            public Class<? extends Throwable> willThrow() {
                throw new UnsupportedOperationException("Mirrored instance; not supported");
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return EgException.class;
            }
        };
        return example(egException, el, am);
    }

    @SuppressWarnings("unchecked")
    private String[] annotationValues(AnnotationMirror am, String paramName) {
        AnnotationValue argListAv = getAnnotationValue(am, paramName);
        List<AnnotationValue> argList = argListAv == null
                ? null
                : (List<AnnotationValue>) argListAv.getValue();

        if (argList==null) {
            return new String[0];
        }
        else {
            List<String> args = argList.stream()
                    .map(AnnotationValue::getValue)
                    .map(it -> (String) it)
                    .collect(Collectors.toList());
            return args.toArray(new String[args.size()]);
        }
    }

    private List<EgItem<?>> example(EgException a, ExecutableElement el, AnnotationMirror egExceptionMirror) {
        AnnotationValue value = getAnnotationValue(egExceptionMirror, "willThrow");
        final TypeName name;
        if (value == null) {
            name = ClassName.get(Throwable.class);
        }
        else {
            TypeMirror typeMirror = (TypeMirror)value.getValue();
            name = ClassName.get(typeMirror);
        }
        return Collections.singletonList(new ExceptionExample(el, a, name));
    }


    // Based on answer by Dave Dopson on StackOverflow:
    // https://stackoverflow.com/questions/7687829/java-6-annotation-processing-getting-a-class-from-an-annotation

    @Nullable
    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ) {
            ExecutableElement method = entry.getKey();
            AnnotationValue value = entry.getValue();
            if (method.getSimpleName().toString().equals(key)) {
                return value;
            }
        }
        return null;
    }
}
