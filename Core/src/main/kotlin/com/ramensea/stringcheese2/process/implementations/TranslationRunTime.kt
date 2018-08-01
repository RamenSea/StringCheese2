package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.interfaces.TextTranslationGenerator
import com.ramensea.stringcheese2.process.interfaces.TextValueSorter

open class TranslationRunTime {
    fun process(options: Options, sorter: TextValueSorter, generator: TextTranslationGenerator,
                languageSets: Array<LanguageTextValueSet>): CompleteOutput {
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
        val rootOutput = generator.process(options, rootSet)
        val outputs = Array<Output>(languageSets.size) { i ->
            val set = languageSets[i]
            if (set.languageShort == rootOutput.languageShort) return@Array rootOutput
            generator.process(options, set)
        }

        //==================
        //Step 5 Generate Key Class
        //==================
        val keyOutput: Output? = if (options.outputKeyClass && generator.supportsKeyClassGeneration) generator.processKeyClass(options,rootSet) else null

        return CompleteOutput(keyOutput,rootOutput,outputs)
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
                " translation set with value:\"${if (options.fillInMissingStringsInBlank) "" else missingTextValue.text}\"\n\n")
    }
}