package com.vocalabs.egtest.processor

object Settings {
        private const val PREFIX = "egtest."
        const val TARGET_LANGUAGE_KEY = PREFIX + "targetLanguage"
        const val TARGET_DIR_KEY = PREFIX + "targetDirectory"
        const val DIR_EXISTS_BEHAVIOR_KEY = PREFIX + "targetDirectoryExistsBehavior"
        const val FAIL_ON_UNSUPPORTED_KEY = PREFIX + "failOnUnsupportedExample"
        const val SELF_TEST_KEY = PREFIX + "selfTest"
}
