package com.ramensea.stringcheese2.models

interface LanguageTextValueSet {
    val languageShort: String
    val textValues: ArrayList<TextValue>
    val alertInfo: StringBuilder
}
data class LanguageTextValueImmutableSet(
        override val languageShort: String,
        override val textValues: ArrayList<TextValue>,
        override val alertInfo: StringBuilder): LanguageTextValueSet {
}
data class LanguageTextValueMutableSet(
        override var languageShort: String,
        override var textValues: ArrayList<TextValue>,
        override var alertInfo: StringBuilder): LanguageTextValueSet {
}