package com.ramensea.stringcheese2.process.implementations.generators

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.interfaces.KeyClassGenerator
import com.ramensea.stringcheese2.process.interfaces.LanguageTextGenerator
import com.ramensea.stringcheese2.process.interfaces.TextValueSorter

open class TranslationRunTime {
    fun<T: Options> process(options: T, sorter: TextValueSorter, languageGenerator: LanguageTextGenerator<T>?,
                            keyClassGenerator: KeyClassGenerator<T>?, languageSets: Array<LanguageTextValueSet>): Array<Output> {
        val outputs: ArrayList<Output> = arrayListOf()

        //==================
        //Step 1 get root value set
        //==================
        val rootSet: LanguageTextValueSet
        var findingRootSet: LanguageTextValueSet? = null
        languageSets.forEach {
            if (it.languageShort == options.rootLanguageId) findingRootSet = it
        }
        rootSet = findingRootSet ?: throw Exception("No root language")

        //==================
        //Step 2 check for missing strings and fill them in
        //==================
        if (options.alertForMissingTexts || options.fillInMissingStrings) {
            //fill into root first
            for (set in languageSets) {
                if (set == rootSet) continue
                checkForMissingAndFill(options,rootSet,set)
            }
            //now fill into the general set
            for (set in languageSets) {
                if (set == rootSet) continue
                checkForMissingAndFill(options,set,rootSet)
            }
        }

        //==================
        //Step 3 Perform sorting
        //==================
        if (options.sortKeys) {
            for (set in languageSets) {
                sorter.sort(set)
            }
        }

        //==================
        //Step 4 Generate output
        //==================
        if (languageGenerator != null && languageGenerator.supportsLanguageFileGeneration) {
            for (set in languageSets) {
                outputs.add(languageGenerator.outputLanguageFile(options, set))
            }
        }

        //==================
        //Step 5 Generate Key Class
        //==================
        if (options.outputKeyClass && keyClassGenerator != null && keyClassGenerator.supportsKeyClassGeneration) {
            outputs.add(keyClassGenerator.outputKeyClass(options,rootSet))
        }

        return outputs.toTypedArray()
    }

    private fun checkForMissingAndFill(options: Options, targetSet: LanguageTextValueSet, checkSet: LanguageTextValueSet) {
        if (targetSet == checkSet) return
        for (setTextValue in checkSet.textValues) {
            var found = false
            for (targetSetValue in targetSet.textValues) {
                if (targetSetValue.key == setTextValue.key) {
                    found = true
                    break
                }
            }
            if (!found) {
                if (options.fillInMissingStrings) targetSet.textValues.add(setTextValue.copy(options.fillInMissingStringsInBlank))
                if (options.alertForMissingTexts) logMissingText(options, targetSet, setTextValue)
            }
        }
    }

    open fun logMissingText(options: Options, targetSet: LanguageTextValueSet, missingTextValue: TextValue) {
        targetSet.alertInfo.append("--${targetSet.languageShort.capitalize()} translation set is missing key: ${missingTextValue.key}.\n")
        if (options.fillInMissingStrings) targetSet.alertInfo.append("String cheese filled ${targetSet.languageShort.capitalize()}'s" +
                " translation set with value:\"${if (options.fillInMissingStringsInBlank) "" else missingTextValue.rawText}\"\n\n")
    }
}