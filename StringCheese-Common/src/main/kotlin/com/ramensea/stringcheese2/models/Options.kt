package com.ramensea.stringcheese2.models

interface Options {
    val timeStamp: String
    val outputPlatform: String
    val outputDir: String
    val rootLanguageId: String

    val alertForMissingTexts: Boolean
    val fillInMissingStrings: Boolean
    val fillInMissingStringsInBlank: Boolean

    val sortKeys: Boolean

    val outputKeyClass: Boolean
    val keyClassName: String
    val keyClassOutputDir: String

    fun getOutputPaths(): Array<String> = outputDir.split("/").toTypedArray()
    fun getKeyClassOutputPaths(): Array<String> = keyClassOutputDir.split("/").toTypedArray()
}