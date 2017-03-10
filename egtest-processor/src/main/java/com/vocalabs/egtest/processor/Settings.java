package com.vocalabs.egtest.processor;

import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Data class for user-defined settings.
 */
public class Settings {
    static final String
            PREFIX = "egtest.",
            TARGET_DIR_KEY = PREFIX+"targetDirectory",
            DIR_EXISTS_BEHAVIOR_KEY = PREFIX+"targetDirectoryExistsBehavior",
            FAIL_ON_UNSUPPORTED_KEY = PREFIX+"failOnUnsupportedExample",
            SELF_TEST_KEY = PREFIX+"selfTest";

    /**
     * The behavior when a target directory already exists. This should be used with caution, since build tools may
     * compile in several phases, especially when multiple JVM languages are in use. For example, if there is a
     * "src/main/java" directory and a "src/main/scala" directory, Gradle compiles them separately.
     */
    public enum AlreadyExistsBehavior {
        /** Write files in place, not changing existing files */ OVERWRITE,
        /**        Delete the target directory before writing */ DELETE,
        /**                                     Don't compile */ FAIL
    }

    private final File targetDir;
    private final AlreadyExistsBehavior targetDirExistsBehavior;
    private final boolean failOnUnsupported;
    private final boolean selfTest;

    static Settings instance(ProcessingEnvironment processingEnv) {
        return instance(processingEnv.getMessager(), processingEnv.getOptions());
    }

    static Settings instance(Messager messager, Map<String,String> options) {
        String targetDirStr = options.getOrDefault(TARGET_DIR_KEY, "");
        if (targetDirStr.equals("")) {
            return illegalInstance("initialization failed; no "+TARGET_DIR_KEY+"; options are "+options);
        }
        String onDirExistsStr = options.get(DIR_EXISTS_BEHAVIOR_KEY);
        AlreadyExistsBehavior targetDirExistsBehavior = AlreadyExistsBehavior.OVERWRITE;
        if (onDirExistsStr != null) {
            try {
                targetDirExistsBehavior = AlreadyExistsBehavior.valueOf(onDirExistsStr);
            }
            catch (IllegalArgumentException ex) {
                return handleBadDirExistsBehavior(messager, onDirExistsStr);
            }
        }
        boolean failOnUnsupported = booleanOption(options.get(FAIL_ON_UNSUPPORTED_KEY), true);
        boolean          selfTest = booleanOption(options.get(SELF_TEST_KEY), false);
        return new Settings(new File(targetDirStr), targetDirExistsBehavior, failOnUnsupported, selfTest);
    }

    private static boolean booleanOption(@Nullable String optionString, boolean defaultValue) {
        if (optionString==null || optionString.isEmpty())
            return defaultValue;
        char ch = Character.toLowerCase(optionString.charAt(0));
        switch (ch) {
            case 'y': return true;
            case 't': return true;
            case 'f': return false;
            case 'n': return false;
            default: throw new IllegalArgumentException("EgTest: not a boolean option: "+optionString);
        }
    }

    private static Settings handleBadDirExistsBehavior(Messager messager, String onDirExistsStr) {
        String legalValues = Arrays.stream(AlreadyExistsBehavior.values())
                .map(Enum::name)
                .reduce((a,b) -> a+", "+b)
                .orElse("(No known values)");
        messager.printMessage(ERROR, "Bad value for "+DIR_EXISTS_BEHAVIOR_KEY+" '"+onDirExistsStr+"', "+
                "legal values are "+legalValues);
        return illegalInstance("initialization failed; bad "+DIR_EXISTS_BEHAVIOR_KEY);
    }


    static Settings illegalInstance(String reason) {
        return new Settings(new File("no-such-file"), AlreadyExistsBehavior.OVERWRITE, true, false) {
            private final String message = "EgTest "+reason;
            @Override public File getTargetDir() { throw new IllegalStateException(message); }
            @Override public boolean isValid() { return false; }
        };
    }

    private Settings(File targetDir, AlreadyExistsBehavior targetDirExistsBehavior, boolean failOnUnsupported, boolean selfTest) {
        this.targetDir = targetDir;
        this.targetDirExistsBehavior = targetDirExistsBehavior;
        this.failOnUnsupported = failOnUnsupported;
        this.selfTest = selfTest;
    }

    /** The location where test source code is written. */
    public File getTargetDir() { return targetDir; }

    public AlreadyExistsBehavior getTargetDirExistsBehavior() { return targetDirExistsBehavior; }

    /** If true, an EgTest annotation which cannot be turned into a unit test causes the build to fail. Default is true. */
    public boolean isFailOnUnsupported() { return failOnUnsupported; }

    /** Returns true if these settings are usable. */
    public boolean isValid() { return true; }

    public boolean isSelfTest() { return selfTest; }
}
