package com.ramensea.stringcheese2.process.interfaces

import com.ramensea.stringcheese2.models.LanguageTextValueSet

interface LanguageFileReader {
    fun readString(s: String): LanguageTextValueSet
}