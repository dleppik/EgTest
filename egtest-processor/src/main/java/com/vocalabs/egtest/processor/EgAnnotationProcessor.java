package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import com.vocalabs.egtest.processor.data.*;
import com.vocalabs.egtest.processor.junit.JUnitWriter;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.vocalabs.egtest.annotation.Eg",
        "com.vocalabs.egtest.annotation.EgContainer",
        "com.vocalabs.egtest.annotation.EgException",
        "com.vocalabs.egtest.annotation.EgExceptionContainer",
        "com.vocalabs.egtest.annotation.EgMatch",
        "com.vocalabs.egtest.annotation.EgMatchContainer",
        "com.vocalabs.egtest.annotation.EgNoMatch",
        "com.vocalabs.egtest.annotation.EgNoMatchContainer",
        "com.vocalabs.egtest.processor.selftest.EgSelfTest"})
@SupportedOptions("egtest.targetDirectory")
public class EgAnnotationProcessor extends AbstractProcessor {

    /** These do the work of pulling examples out of annotations. */
    private static final List<AnnotationReader<?>> EXAMPLE_HANDLERS = Arrays.asList(
            ReturnsReader.INSTANCE,
            MatchReader.INSTANCE,
            NotSupportedReader.INSTANCE,
            IgnoredReader.INSTANCE);

    private MessageHandler messageHandler = null;
    private boolean firstPass = true;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messageHandler = new MessageHandler(processingEnv.getMessager(), false);
        firstPass = true;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            AnnotationCollector collector = new AnnotationCollector(messageHandler);
            checkFactories();  // Eventually we'll want to do this only on self-test
            EXAMPLE_HANDLERS.forEach(f -> f.addExamples(roundEnv, collector));

            File targetDir = new File(processingEnv.getOptions().get("egtest.targetDirectory"));
            EgTestWriter.AlreadyExistsBehavior onExists = (firstPass)
                    ? EgTestWriter.AlreadyExistsBehavior.DELETE
                    : EgTestWriter.AlreadyExistsBehavior.OVERWRITE;
            new JUnitWriter(targetDir, onExists).write(collector);
        }
        catch (Exception ex) {
            messageHandler.error(ex);
        }
        firstPass = false;
        return true;
    }

    private void checkFactories() {
        Map<String,AnnotationReader<?>> found = new HashMap<>();
        for (AnnotationReader<?> factory: EXAMPLE_HANDLERS) {
            Set<Class<? extends Annotation>> current = factory.supportedAnnotationClasses();
            for (Class<? extends Annotation> a : current) {
                String name = a.getCanonicalName();
                if (found.containsKey(name))
                    messageHandler.error("Bug: Redundant support for "+name+": in "+found.get(name)+" and "+factory);
                found.put(name, factory);
            }
        }

        for (String annotated: getClass().getAnnotation(SupportedAnnotationTypes.class).value()) {
            if (! found.containsKey(annotated)) {
                messageHandler.error("Bug: EgTest has no handler for "+annotated);
            }
            found.remove(annotated);
        }

        for (String unhandled: found.keySet()) {
            messageHandler.error("Bug: EgTest claims to support '"+unhandled+"' but it has no handler");
        }
    }
}
