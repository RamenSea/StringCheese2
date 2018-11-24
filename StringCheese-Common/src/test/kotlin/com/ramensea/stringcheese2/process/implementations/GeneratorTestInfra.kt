package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.Options
import com.ramensea.stringcheese2.models.TextValue
import com.ramensea.stringcheese2.process.interfaces.KeyClassGenerator
import com.ramensea.stringcheese2.process.interfaces.LanguageTextGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class GeneratorTestInfra<T: Options> {
    abstract val keysToTest: Array<KeyToPlatformKey>

    abstract fun getLanguageTextGenerator(): LanguageTextGenerator<T>?
    abstract fun getKeyClassGenerator(): KeyClassGenerator<T>?
    abstract fun getOptions(): T

    @Test
    fun testGeneratorsKeyToPlatformKey() {
        val generator = getLanguageTextGenerator() ?: return
        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.translateKey(keyToPlatformKey.textValue.key),keyToPlatformKey.actualKey)
        }
    }
    @Test
    fun testGeneratorsLanguageFileLine() {
        val generator = getLanguageTextGenerator() ?: return
        if (!generator.supportsLanguageFileGeneration) return
        val options = getOptions()
        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.generateLanguageFileLine(options,keyToPlatformKey.textValue),keyToPlatformKey.actualLanguageFileLine)
        }
    }

    @Test
    fun testGeneratorsKeyToKeyClassMethod() {
        val generator = getKeyClassGenerator() ?: return
        if (!generator.supportsKeyClassGeneration) return

        for (keyToPlatformKey in keysToTest) {
            assertEquals(generator.translateKeyToClassMethod(keyToPlatformKey.textValue.key),keyToPlatformKey.actualKeyClassMethodName)
        }
    }

    @Test
    fun testGeneratorsKeyClassMethod() {
        val generator = getKeyClassGenerator() ?: return
        if (!generator.supportsKeyClassGeneration) return
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