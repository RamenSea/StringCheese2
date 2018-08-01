package com.ramensea.stringcheese2.models

interface Options {
    val timeStamp: String
    val outputPlatform: String
    val rootLanguageId: String

    val alertForMissingTexts: Boolean
    val fillInMissingStrings: Boolean
    val fillInMissingStringsInBlank: Boolean

    val sortKeys: Boolean

    val outputKeyClass: Boolean
    val keyClassName: String
}