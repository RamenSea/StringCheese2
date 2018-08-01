package com.ramensea.stringcheese2.models

interface Output {
    val platformId: String
    val languageShort: String
    val text: String
    val isKeyFile: Boolean

    companion object {
        const val ALL_LANGUAGE_ID = "ALL"
    }
}

data class GeneralOutput(override val platformId: String,
                         override val languageShort: String,
                         override val text: String,
                         override val isKeyFile: Boolean):Output

data class CompleteOutput(val keyClassOutput: Output?,
                          val rootOutput: Output,
                          val outputs: Array<Output>)