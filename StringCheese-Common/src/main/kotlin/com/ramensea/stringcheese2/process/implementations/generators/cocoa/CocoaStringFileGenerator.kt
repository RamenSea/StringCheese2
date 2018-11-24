package com.ramensea.stringcheese2.process.implementations.generators.cocoa

import com.ramensea.stringcheese2.extensions.lowerFirstCharacter
import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.implementations.generators.StringSanitizer
import com.ramensea.stringcheese2.process.interfaces.LanguageTextGenerator

class CocoaStringFileGenerator(private val argumentReader: ArgumentReader,
                               val sanitizer: StringSanitizer): LanguageTextGenerator<CocoaOptions> {
    override val supportsLanguageFileGeneration: Boolean = true
    private val classArguments = CocoaTouchKeyClassArguments(sanitizer)

    override fun translateKey(key: String): String {
        return sanitizer.sanitizeKey(key).lowerFirstCharacter().trim().replace("\\s+".toRegex(), " ").replace(" ", "_").replace("_[A-Z]".toRegex(), { matchResult ->
            matchResult.value.toLowerCase()
        }).replace("[A-Z]".toRegex(), { matchResult ->
            "_" + matchResult.value.toLowerCase()
        })
    }

    override fun outputLanguageFile(options: CocoaOptions, languageSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("//${options.timeStamp}\n\n")
        for (tv in languageSet.textValues) {
            builder.append(generateLanguageFileLine(options, tv))
        }
        val folderName = if (options.rootLanguageId == languageSet.languageShort) "/Base.lproj" else "/${languageSet.languageShort.capitalize()}.lproj"
        return GeneralOutput(options.outputPlatform, languageSet.languageShort, builder.toString(),options.dotStringFileName,options.outputDir + folderName, false)
    }

    override fun generateLanguageFileLine(options: CocoaOptions, value: TextValue): String {
        if (value.hasArguments()) return "\"${translateKey(value.key)}\" = \"${classArguments.writeArgumentText(argumentReader,value)}\";\n"
        else return "\"${translateKey(value.key)}\" = \"${sanitizer.sanitizeText(value.rawText)}\";\n"
    }

}
