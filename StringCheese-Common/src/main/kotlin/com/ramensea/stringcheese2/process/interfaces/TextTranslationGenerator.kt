package com.ramensea.stringcheese2.process.interfaces

import com.ramensea.stringcheese2.models.LanguageTextValueSet
import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.Output
import com.ramensea.stringcheese2.models.TextValue


interface LanguageTextGenerator<T: Options> {
    val supportsLanguageFileGeneration: Boolean
    fun translateKey(key: String): String
    fun outputLanguageFile(options: T, languageSet: LanguageTextValueSet): Output
    fun generateLanguageFileLine(options: T, value: TextValue): String
}
interface KeyClassGenerator<T: Options> {
    val supportsKeyClassGeneration: Boolean
    fun translateKeyToClassMethod(key: String): String
    fun outputKeyClass(options: T, rootSet: LanguageTextValueSet): Output
    fun generateKeyClassMethod(options: T, value: TextValue): String
}
interface TextValueSorter {
    fun sort(set: LanguageTextValueSet)
}