package com.ramensea.stringcheese2.process.implementations

import kotlin.test.Test
import kotlin.test.assertEquals

class AndroidGeneratorKeyTranslationTests {

    @Test
    fun `Test translate key for Android xml`() {
        val androidGenerator = AndroidGenerator()
        assertEquals(androidGenerator.translateKey("Rar r rRar r"),"rar_r_r_rar_r")
        assertEquals(androidGenerator.translateKey("TestTTest"),"test_t_test")
        assertEquals(androidGenerator.translateKey("TT TtT"),"t_t_tt_t","Original key was \"TT TtT\"")
        assertEquals(androidGenerator.translateKey("TT   TtT"),"t_t_tt_t","Original key was \"TT   TtT\"")
        assertEquals(androidGenerator.translateKey("TT  TtT"),"t_t_tt_t","Original key was \"TT  TtT\"")
        assertEquals(androidGenerator.translateKey("TT  T   t       T"),"t_t_t_t_t","Original key was \"TT  T   t       T\"")
    }
    @Test
    fun `Test translate function name for Kotlin key class`() {
        val androidGenerator = AndroidGenerator()
        assertEquals(androidGenerator.translateKeyToClassMethod("Rar r rRar r"),"rarRRRarR")
        assertEquals(androidGenerator.translateKeyToClassMethod("Rar r rRar r",true),"RarRRRarR")
        assertEquals(androidGenerator.translateKeyToClassMethod("TestTTest"),"testTTest")
        assertEquals(androidGenerator.translateKeyToClassMethod("TT TtT"),"tTTtT","Original key was \"TT TtT\"")
        assertEquals(androidGenerator.translateKeyToClassMethod("TT   TtT"),"tTTtT","Original key was \"TT   TtT\"")
        assertEquals(androidGenerator.translateKeyToClassMethod("TT  TtT"),"tTTtT","Original key was \"TT  TtT\"")
        assertEquals(androidGenerator.translateKeyToClassMethod("TT  T   t       T"),"tTTTT","Original key was \"TT  T   t       T\"")
    }
}