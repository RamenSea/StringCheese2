package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.interfaces.TextTranslationGenerator

class CocoaTouchGenerator: TextTranslationGenerator {
    override val supportsLanguageFile: Boolean = true
    override val supportsKeyClassGeneration: Boolean = true
    private val classArguments = CocoaTouchKeyClassArguments()

    override fun translateKey(key: String): String {
        return key.lowercaseFirstCharacter().trim().replace("\\s+".toRegex(), " ").replace(" ", "_").replace("_[A-Z]".toRegex(), { matchResult ->
            matchResult.value.toLowerCase()
        }).replace("[A-Z]".toRegex(), { matchResult ->
            "_" + matchResult.value.toLowerCase()
        })
    }

    override fun translateKeyToClassMethod(key: String): String {
        return translateKey(key).replace("_.".toRegex(), { matchResult ->
            matchResult.value.substring(1).toUpperCase()
        })
    }


    override fun outputLanguageFile(options: Options, languageSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("//String Cheese generated at:${options.timeStamp}\n\n")
        for (tv in languageSet.textValues) {
            builder.append(generateLanguageFileLine(options, tv))
        }
        return GeneralOutput(options.outputPlatform, languageSet.languageShort, builder.toString(), false)
    }

    override fun generateLanguageFileLine(options: Options, value: TextValue): String {
        return "\"${translateKey(value.key)}\" = \"${value.getText(classArguments)}\";\n"
    }

    override fun outputKeyClass(options: Options, rootSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("//String Cheese generated at:${options.timeStamp}\n" +
                "import Foundation\n" +
                "\n" +
                "class ${options.keyClassName} {" +
                "\tprivate func localize(_ key: String) -> String {\n" +
                "\t\treturn NSLocalizedString(key, comment: \"\")\n" +
                "\t}\n" +
                "\n")

        for (textValue in rootSet.textValues) {
            builder.append(generateKeyClassMethod(options,textValue))
        }

        return GeneralOutput(options.outputPlatform,"",builder.toString(),true)
    }

    override fun generateKeyClassMethod(options: Options, value: TextValue): String {
        val methodName = translateKeyToClassMethod(value.key)
        if (!value.hasArguments() && value.arguments != null) {
            var s: String
            if (value.isTranslatable) {
                s = "\tlet raw_$methodName: String {\n" +
                        "\t\treturn localize(\"$methodName\")\n" +
                        "\t}\n"
            } else {
                s = "\tlet raw_$methodName: String = \"${value.getArgumentString(classArguments)}\""
            }
            s += "\tfunc $methodName(${classArguments.getArgumentList(value.arguments)}) -> String {\n" +
                    "\t\treturn String(format: raw_$methodName, ${classArguments.getArgumentList(value.arguments)})" +
                    "\t}"
            return s
        } else {
            if (value.isTranslatable) {
                return "\tlet $methodName: String {\n" +
                        "\t\treturn localize(\"${translateKey(value.key)}\")\n" +
                        "\t}\n"
            } else {
                return "\tlet $methodName: String = \"${value.getText()}\""
            }
        }
    }

    data class CocoaOptions(override val timeStamp: String,
                              override val rootLanguageId: String,

                              override val alertForMissingTexts: Boolean,
                              override val fillInMissingStrings: Boolean,
                              override val fillInMissingStringsInBlank: Boolean,

                              override val sortKeys: Boolean,

                              override val outputKeyClass: Boolean,
                              override val keyClassName: String) : Options {

        override val outputPlatform: String = "Cocoa"
    }
}

class CocoaTouchKeyClassArguments: ClassArgumentWriter {
    override fun getFormatString(index: Int): String = "%@"
    override fun getFormatInt(index: Int): String = "%d"
    override fun getFormatLong(index: Int): String = "%ld"
    override fun getFormatFloat(index: Int, decimalPlace: Int): String = if (decimalPlace < 0) "%f" else "%.${decimalPlace}f"
    override fun getFormatDouble(index: Int, decimalPlace: Int): String = if (decimalPlace < 0) "%f" else "%.${decimalPlace}f"

    override fun getArgumentTypeName(type: Argument.Types): String {
        return when (type) {
            Argument.Types.STRING -> "String"
            Argument.Types.INT -> "Int"
            Argument.Types.LONG -> "Int"
            Argument.Types.FLOAT -> "Float"
            Argument.Types.DOUBLE -> "Double"
        }
    }

    override fun getArgumentList(arguments: Array<Argument>): String {
        val builder = StringBuilder()
        var isFirst = true
        for (argument in arguments) {
            builder.append("${if (!isFirst)" ," else ""}${argument.name}: ${getArgumentTypeName(argument.type)}")
            isFirst = false
        }
        return builder.toString()
    }

    override fun getArgumentListForFormat(arguments: Array<Argument>): String {
        val builder = StringBuilder()
        var isFirst = true
        for (argument in arguments) {
            builder.append("${if (!isFirst)" ," else ""}${argument.name}")
            isFirst = false
        }
        return builder.toString()
    }
}