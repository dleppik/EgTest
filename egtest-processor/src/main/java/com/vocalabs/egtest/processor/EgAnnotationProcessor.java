package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import com.vocalabs.egtest.processor.data.*;
import com.vocalabs.egtest.processor.junit.AllClassesWriter;
import com.vocalabs.egtest.processor.selftest.SelfTestMessageHandler;

import java.lang.annotation.Annotation;
import java.util.*;

import static javax.tools.Diagnostic.Kind.WARNING;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.vocalabs.egtest.annotation.Eg",
        "com.vocalabs.egtest.annotation.EgContainer",
        "com.vocalabs.egtest.annotation.EgDefaultLanguage",
        "com.vocalabs.egtest.annotation.EgException",
        "com.vocalabs.egtest.annotation.EgExceptionContainer",
        "com.vocalabs.egtest.annotation.EgMatch",
        "com.vocalabs.egtest.annotation.EgMatchContainer",
        "com.vocalabs.egtest.annotation.EgNoMatch",
        "com.vocalabs.egtest.annotation.EgNoMatchContainer",
        "com.vocalabs.egtest.processor.selftest.EgSelfTest"})
@SupportedOptions({
        Settings.TARGET_DIR_KEY,
        Settings.DIR_EXISTS_BEHAVIOR_KEY,
        Settings.FAIL_ON_UNSUPPORTED_KEY,
        Settings.SELF_TEST_KEY,
        Settings.TARGET_LANGUAGE_KEY})
public class EgAnnotationProcessor extends AbstractProcessor {

    /** These do the work of pulling examples out of annotations. */
    private static final List<AnnotationReader<?>> ANNOTATION_READERS = Arrays.asList(
            ReturnsReader.INSTANCE,
            MatchReader.INSTANCE,
            ExceptionReader.INSTANCE,
            IgnoredReader.INSTANCE,
            DefaultLanguageReader.INSTANCE);

    private MessageHandler messageHandler = null;
    private boolean firstPass = true;
    private Settings settings = Settings.illegalInstance("not initialized");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        settings = Settings.instance(processingEnv);

        messageHandler = (settings.isSelfTest())
                ? new SelfTestMessageHandler(processingEnv.getMessager())
                : new MessageHandler(processingEnv.getMessager(), settings.isFailOnUnsupported());
        if (settings.isValid()) {
            messageHandler.note("EgTest will write test source code in "+settings.getTargetDir());
        }
        else if ( ! processingEnv.getOptions().containsKey(Settings.TARGET_DIR_KEY)) {
            processingEnv.getMessager().printMessage(WARNING, "Skipping EgTest, "+Settings.TARGET_DIR_KEY+" not specified");
        }
        firstPass = true;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            if (! settings.isValid()) {
                return true;
            }

            if (settings.isSelfTest()) {
                checkAnnotationReaders();
            }
            AnnotationCollector collector = new AnnotationCollector(messageHandler);
            ANNOTATION_READERS.forEach(f -> f.addExamples(roundEnv, collector));

            final Settings.AlreadyExistsBehavior onExists = firstPass
                    ? settings.getTargetDirExistsBehavior()
                    : Settings.AlreadyExistsBehavior.OVERWRITE;

            new AllClassesWriter(settings.getLanguage(), onExists, settings.getTargetDir()).write(collector);
            if (messageHandler instanceof SelfTestMessageHandler) {
                ((SelfTestMessageHandler) messageHandler).write(settings.getTargetDir().toPath());
            }
        }
        catch (Exception ex) {
            messageHandler.error(ex);
        }
        firstPass = false;
        return true;
    }


    private void checkAnnotationReaders() {
        Map<String,AnnotationReader<?>> found = new HashMap<>();
        for (AnnotationReader<?> reader: ANNOTATION_READERS) {
            Set<Class<? extends Annotation>> current = reader.supportedAnnotationClasses();
            for (Class<? extends Annotation> a : current) {
                String name = a.getCanonicalName();
                if (found.containsKey(name))
                    messageHandler.error("Bug: Redundant support for "+name+": in "+found.get(name)+" and "+reader);
                found.put(name, reader);
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
