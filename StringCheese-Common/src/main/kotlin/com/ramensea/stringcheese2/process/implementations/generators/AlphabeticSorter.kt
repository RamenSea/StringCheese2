package com.ramensea.stringcheese2.process.implementations.generators

import com.ramensea.stringcheese2.models.LanguageTextValueSet
import com.ramensea.stringcheese2.models.TextValue
import com.ramensea.stringcheese2.process.interfaces.TextValueSorter


class AlphabeticSorter: TextValueSorter {
    private val alphabeticalSort = compareBy<TextValue> { it.key }

    override fun sort(set: LanguageTextValueSet) {
        set.textValues.sortWith(alphabeticalSort)
    }
}