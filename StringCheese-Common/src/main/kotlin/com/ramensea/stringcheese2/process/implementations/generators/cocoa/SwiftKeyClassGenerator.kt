package com.ramensea.stringcheese2.process.implementations.generators.cocoa

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.implementations.generators.StringSanitizer
import com.ramensea.stringcheese2.process.interfaces.KeyClassGenerator


class SwiftKeyClassGenerator(private val argumentReader: ArgumentReader,
                             private val cocoaStringFileGenerator: CocoaStringFileGenerator): KeyClassGenerator<CocoaOptions> {
    override val supportsKeyClassGeneration: Boolean = true
    private val classArguments = CocoaTouchKeyClassArguments(cocoaStringFileGenerator.sanitizer)

    override fun translateKeyToClassMethod(key: String): String {
        return cocoaStringFileGenerator.translateKey(key).replace("_.".toRegex(), { matchResult ->
            matchResult.value.substring(1).toUpperCase()
        })
    }

    override fun outputKeyClass(options: CocoaOptions, rootSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("//${options.timeStamp}\n" +
                "import Foundation\n" +
                "\n" +
                "class ${options.keyClassName}${if (options.objCCompatInKeyClass) ": NSObject" else ""} {\n" +
                "\tprivate func localize(_ key: String) -> String {\n" +
                "\t\treturn NSLocalizedString(key, comment: \"\")\n" +
                "\t}\n" +
                "\n")

        for (textValue in rootSet.textValues) {
            builder.append(generateKeyClassMethod(options,textValue))
        }
        builder.append("}\n")

        return GeneralOutput(options.outputPlatform,"",builder.toString(),"${options.keyClassName}.swift",options.keyClassOutputDir, true)
    }

    override fun generateKeyClassMethod(options: CocoaOptions, value: TextValue): String {
        val methodName = translateKeyToClassMethod(value.key)
        val arguments = value.arguments
        val addObjCCompat = if (options.objCCompatInKeyClass) "@objc " else ""
        if (value.hasArguments() && arguments != null) {
            var s: String
            if (value.isTranslatable) {
                s = "\t${addObjCCompat}public var raw_$methodName: String {\n" +
                        "\t\treturn localize(\"${cocoaStringFileGenerator.translateKey(value.key)}\")\n" +
                        "\t}\n"
            } else {
                s = "\t${addObjCCompat}public let raw_$methodName: String = \"${classArguments.writeArgumentText(argumentReader,value)}\"\n"
            }
            s += "\t${addObjCCompat}public func $methodName(${classArguments.getArgumentList(arguments)}) -> String {\n" +
                    "\t\treturn String(format: raw_$methodName, ${classArguments.getArgumentListForFormat(arguments)})\n" +
                    "\t}\n"
            return s
        } else {
            if (value.isTranslatable) {
                return "\t${addObjCCompat}public var $methodName: String {\n" +
                        "\t\treturn localize(\"${cocoaStringFileGenerator.translateKey(value.key)}\")\n" +
                        "\t}\n"
            } else {
                return "\t${addObjCCompat}public let $methodName: String = \"${cocoaStringFileGenerator.sanitizer.sanitizeText(value.rawText)}\"\n"
            }
        }
    }
}
