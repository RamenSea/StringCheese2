package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.TextValue
import com.ramensea.stringcheese2.process.implementations.generators.cocoa.CocoaOptions
import com.ramensea.stringcheese2.process.implementations.generators.cocoa.CocoaStringFileGenerator
import com.ramensea.stringcheese2.process.implementations.generators.cocoa.SwiftKeyClassGenerator
import com.ramensea.stringcheese2.process.interfaces.KeyClassGenerator
import com.ramensea.stringcheese2.process.interfaces.LanguageTextGenerator

class CocoaTouchGeneratorTest: GeneratorTestInfra<CocoaOptions>() {
    override fun getOptions(): CocoaOptions = CocoaOptions("timestamp","en",
            true,true,true,true,true,true,"StringCheese")
    override val keysToTest: Array<KeyToPlatformKey> =
            arrayOf(KeyToPlatformKey(TextValue(true,"This is A test","the text value")
                    , "this_is_a_test","\"this_is_a_test\" = \"the text value\";\n",
                    "thisIsATest","\tlet thisIsATest: String {\n" +
                    "\t\treturn localize(\"this_is_a_test\")\n" +
                    "\t}\n"),
                    KeyToPlatformKey(TextValue(true,"ANOTHER","the text value")
                    , "a_n_o_t_h_e_r","\"a_n_o_t_h_e_r\" = \"the text value\";\n",
                    "aNOTHER","\tlet aNOTHER: String {\n" +
                    "\t\treturn localize(\"a_n_o_t_h_e_r\")\n" +
                    "\t}\n"))


    override fun getLanguageTextGenerator(): LanguageTextGenerator<CocoaOptions>? {
        return CocoaStringFileGenerator()
    }
    override fun getKeyClassGenerator(): KeyClassGenerator<CocoaOptions>? {
        return SwiftKeyClassGenerator(CocoaStringFileGenerator())
    }
}