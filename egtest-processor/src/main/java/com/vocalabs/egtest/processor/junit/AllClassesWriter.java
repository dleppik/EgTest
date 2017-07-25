package com.vocalabs.egtest.processor.junit;

import com.vocalabs.egtest.annotation.EgLanguage;
import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.EgTestWriter;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.Settings;
import com.vocalabs.egtest.processor.data.EgItem;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/** Build JUnit test source code; this is the code generator entry point. */
public class AllClassesWriter implements EgTestWriter {

    private final File directoryToFill;
    private final Settings.AlreadyExistsBehavior directoryExistsBehavior;
    private final EgLanguage defaultLanguage;

    public AllClassesWriter(EgLanguage defaultLanguage,
                            Settings.AlreadyExistsBehavior directoryExistsBehavior,
                            File directoryToFill) {
        this.directoryToFill = directoryToFill;
        this.directoryExistsBehavior = directoryExistsBehavior;
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public void write(AnnotationCollector annotationCollector) throws Exception {
        if (Settings.AlreadyExistsBehavior.FAIL.equals(directoryExistsBehavior) && directoryToFill.exists()) {
            annotationCollector.getMessageHandler()
                    .error("EgTest target directory exists ("+directoryToFill.getAbsolutePath()+")");
            return;
        }

        boolean didCreateDir = directoryToFill.mkdirs();
        if (Settings.AlreadyExistsBehavior.DELETE.equals(directoryExistsBehavior) &&  ! didCreateDir) {
            File[] files = directoryToFill.listFiles();
            if (files == null)
                throw new IOException("Location for writing EgTest source is not a directory: "+directoryToFill);
            for (File f: files) {
                deleteRecursive(f);
            }
        }

        MessageHandler messageHandler = annotationCollector.getMessageHandler();
        Map<String, List<EgItem<?>>> itemsByClassName = annotationCollector.getItemsByClassName();
        for (Map.Entry<String, List<EgItem<?>>> entry: itemsByClassName.entrySet()) {
            String className = entry.getKey();
            List<EgItem<?>> items = entry.getValue();
            Map<String,EgLanguage> languageForClassName = annotationCollector.getLanguageForClassName();
            ClassWriter.createFileSpec(languageForClassName, defaultLanguage, className, messageHandler, items)
                .writeTo(directoryToFill);
        }
    }

    private void deleteRecursive(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f: files) {
                deleteRecursive(f);
            }
        }
        file.delete();
    }
}
