package com.ramensea.stringcheese2.models

interface LanguageTextValueSet {
    val languageShort: String
    val textValues: ArrayList<TextValue>
    var alertInfo: StringBuilder
}