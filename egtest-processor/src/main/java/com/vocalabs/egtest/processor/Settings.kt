package com.vocalabs.egtest.processor

import com.vocalabs.egtest.annotation.EgLanguage
import java.io.File
import java.util.*
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic.Kind.ERROR

/**
 * Data class for user-defined settings.
 */
data class Settings(val language: EgLanguage,
                    /** The location where test source code is written.  */
                    val targetDir: File,
                    val targetDirExistsBehavior: AlreadyExistsBehavior,
                    /** If true, an EgTest annotation which cannot be turned into a unit test causes the build to fail. Default is true.  */
                    val isFailOnUnsupported: Boolean,
                    val isSelfTest: Boolean,
                    /** Returns true if these settings are usable, false if EgTest is misconfigured.  */
                    val isValid: Boolean = true,
                    val invalidReason: String? = null) {

    /**
     * The behavior when a target directory already exists. This should be used with caution, since build tools may
     * compile in several phases, especially when multiple JVM languages are in use. For example, if there is a
     * "src/main/java" directory and a "src/main/scala" directory, Gradle compiles them separately.
     */
    enum class AlreadyExistsBehavior {
        /** Write files in place, not changing existing files  */
        OVERWRITE,
        /**        Delete the target directory before writing  */
        DELETE,
        /**                                     Don't compile  */
        FAIL
    }

    companion object {
        private const val PREFIX = "egtest."
        const val TARGET_LANGUAGE_KEY = PREFIX + "targetLanguage"
        const val TARGET_DIR_KEY = PREFIX + "targetDirectory"
        const val DIR_EXISTS_BEHAVIOR_KEY = PREFIX + "targetDirectoryExistsBehavior"
        const val FAIL_ON_UNSUPPORTED_KEY = PREFIX + "failOnUnsupportedExample"
        const val SELF_TEST_KEY = PREFIX + "selfTest"

        fun instance(processingEnv: ProcessingEnvironment): Settings {
            return instance(processingEnv.messager, processingEnv.options)
        }

        fun instance(messager: Messager, options: Map<String, String>): Settings {
            val targetDirStr = options.getOrDefault(TARGET_DIR_KEY, "")
            if (targetDirStr == "") {
                return illegalInstance("initialization failed; no $TARGET_DIR_KEY; options are $options")
            }
            val onDirExistsStr = options[DIR_EXISTS_BEHAVIOR_KEY]
            var targetDirExistsBehavior = AlreadyExistsBehavior.OVERWRITE
            if (onDirExistsStr != null) {
                try {
                    targetDirExistsBehavior = AlreadyExistsBehavior.valueOf(onDirExistsStr)
                } catch (ex: IllegalArgumentException) {
                    return handleBadDirExistsBehavior(messager, onDirExistsStr)
                }

            }
            val language = EgLanguage.valueOf(options.getOrDefault(TARGET_LANGUAGE_KEY, "JAVA"))
            val failOnUnsupported = booleanOption(options[FAIL_ON_UNSUPPORTED_KEY], true)
            val selfTest = booleanOption(options[SELF_TEST_KEY], false)
            return Settings(language, File(targetDirStr), targetDirExistsBehavior, failOnUnsupported, selfTest)
        }

        private fun booleanOption(optionString: String?, defaultValue: Boolean): Boolean {
            if (optionString == null || optionString.isEmpty())
                return defaultValue
            val ch = Character.toLowerCase(optionString[0])
            return when (ch) {
                'y' -> true
                't' -> true
                'f' -> false
                'n' -> false
                else -> throw IllegalArgumentException("EgTest: not a boolean option: $optionString")
            }
        }

        private fun handleBadDirExistsBehavior(messager: Messager, onDirExistsStr: String): Settings {
            val legalValues = Arrays.stream(AlreadyExistsBehavior.values())
                    .map<String>({ it.name })
                    .reduce { a, b -> "$a, $b" }
                    .orElse("(No known values)")
            messager.printMessage(ERROR, "Bad value for $DIR_EXISTS_BEHAVIOR_KEY '$onDirExistsStr', legal values are $legalValues")
            return illegalInstance("initialization failed; bad $DIR_EXISTS_BEHAVIOR_KEY")
        }

        fun illegalInstance(reason: String): Settings {
            return Settings(
                    EgLanguage.JAVA,
                    File("no-such-file"),
                    AlreadyExistsBehavior.OVERWRITE,
                    isFailOnUnsupported = true,
                    isSelfTest = false,
                    isValid = false,
                    invalidReason = reason
            )
        }
    }
}
