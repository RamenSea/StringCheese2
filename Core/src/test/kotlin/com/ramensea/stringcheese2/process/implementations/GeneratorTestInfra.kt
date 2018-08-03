package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.TextValue
import com.ramensea.stringcheese2.process.interfaces.TextTranslationGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class GeneratorTestInfra {
    abstract val keysToTest: Array<KeyToPlatformKey>

    abstract fun getGenerator(): TextTranslationGenerator
    abstract fun getOptions(): Options

    @Test
    fun testGeneratorsKeyToPlatformKey() {
        val generator = getGenerator()
        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.translateKey(keyToPlatformKey.textValue.key),keyToPlatformKey.actualKey)
        }
    }
    @Test
    fun testGeneratorsLanguageFileLine() {
        val generator = getGenerator()
        if (!generator.supportsLanguageFile) return
        val options = getOptions()
        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.generateLanguageFileLine(options,keyToPlatformKey.textValue),keyToPlatformKey.actualLanguageFileLine)
        }
    }

    @Test
    fun testGeneratorsKeyToKeyClassMethod() {
        val generator = getGenerator()
        if (!generator.supportsKeyClassGeneration) return

        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.translateKeyToClassMethod(keyToPlatformKey.textValue.key),keyToPlatformKey.actualKeyClassMethodName)
        }
    }

    @Test
    fun testGeneratorsKeyClassMethod() {
        val generator = getGenerator()
        if (!generator.supportsLanguageFile) return
        val options = getOptions()
        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.generateKeyClassMethod(options,keyToPlatformKey.textValue),keyToPlatformKey.actualKeyClassMethod)
        }
    }
    data class KeyToPlatformKey(val textValue: TextValue,
                                val actualKey: String,
                                val actualLanguageFileLine: String,
                                val actualKeyClassMethodName: String,
                                val actualKeyClassMethod: String)
}