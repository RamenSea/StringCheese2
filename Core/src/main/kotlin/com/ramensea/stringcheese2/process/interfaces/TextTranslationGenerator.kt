package com.ramensea.stringcheese2.process.interfaces

import com.ramensea.stringcheese2.models.LanguageTextValueSet
import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.Output
import com.ramensea.stringcheese2.models.TextValue


interface TextTranslationGenerator {
    fun translateKey(key: String): String
    val supportsLanguageFile: Boolean
    fun outputLanguageFile(options: Options, languageSet: LanguageTextValueSet): Output
    fun generateLanguageFileLine(options: Options, value: TextValue): String

    val supportsKeyClassGeneration: Boolean
    fun outputKeyClass(options: Options, rootSet: LanguageTextValueSet): Output
    fun generateKeyClassMethod(options: Options, value: TextValue): String
    fun translateKeyToClassMethod(key: String): String
}
interface TextValueSorter {
    fun sort(set: LanguageTextValueSet)
}