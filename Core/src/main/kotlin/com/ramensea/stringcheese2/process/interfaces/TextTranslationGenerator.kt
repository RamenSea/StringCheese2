package com.ramensea.stringcheese2.process.interfaces

import com.ramensea.stringcheese2.models.LanguageTextValueSet
import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.Output


interface TextTranslationGenerator {
    val supportsKeyClassGeneration: Boolean
    fun translateKey(key: String): String
    fun process(options: Options, languageSet: LanguageTextValueSet): Output
    fun processKeyClass(options: Options, rootSet: LanguageTextValueSet): Output
}
interface TextValueSorter {
    fun sort(set: LanguageTextValueSet)
}