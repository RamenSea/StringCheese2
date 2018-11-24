package com.ramensea.stringcheese2.process.implementations.generators.cocoa

import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.process.expect.getTimeStamp

data class CocoaOptions(override val timeStamp: String = getTimeStamp(),
                        override val rootLanguageId: String,
                        override val outputDir: String,

                        override val alertForMissingTexts: Boolean,
                        override val fillInMissingStrings: Boolean,
                        override val fillInMissingStringsInBlank: Boolean,

                        override val sortKeys: Boolean,

                        override val outputKeyClass: Boolean,
                        override val keyClassName: String,
                        override val keyClassOutputDir: String,

                        val objCCompatInKeyClass: Boolean,

                        val dotStringFileName: String = "Localizable.strings") : Options {

    override val outputPlatform: String = "Cocoa"
}