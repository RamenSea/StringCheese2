package com.ramensea.stringcheese2.process.implementations

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.interfaces.TextTranslationGenerator

class CocoaTouchGenerator: TextTranslationGenerator {
    override val supportsKeyClassGeneration: Boolean = true
    private val classArguments = CocoaTouchKeyClassArguments()

    override fun translateKey(key: String): String {
        return key.lowercaseFirstCharacter().trim().replace("\\s+".toRegex(), " ").replace(" ", "_").replace("_[A-Z]".toRegex(), { matchResult ->
            matchResult.value.toLowerCase()
        }).replace("[A-Z]".toRegex(), { matchResult ->
            "_" + matchResult.value.toLowerCase()
        })
    }

    fun translateKeyToClassMethod(key: String): String {
        return translateKey(key).replace("_.".toRegex(), { matchResult ->
            matchResult.value.substring(1).toUpperCase()
        })
    }


    override fun process(options: Options, languageSet: LanguageTextValueSet): Output {
        val builder = StringBuilder()
        builder.append("//String Cheese generated at:${options.timeStamp}\n\n")
        for (tv in languageSet.textValues) {
            builder.append("\"${translateKey(tv.key)}\" = \"${tv.getText(classArguments)}\"")
        }
        return GeneralOutput(options.outputPlatform, languageSet.languageShort, builder.toString(), false)
    }

    override fun processKeyClass(options: Options, rootSet: LanguageTextValueSet): Output {
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
            val methodName = translateKeyToClassMethod(textValue.key)
            if (!textValue.hasArguments() && textValue.arguments != null) {
                if (textValue.isTranslatable) {
                    builder.append("\tlet raw_$methodName: String {\n" +
                            "\t\treturn localize(\"$methodName\")\n" +
                            "\t}\n")
                } else {
                    builder.append("\tlet raw_$methodName: String = \"${textValue.getArgumentString(classArguments)}\"")
                }
                builder.append("\tfunc $methodName(${classArguments.getArgumentList(textValue.arguments)}) -> String {\n" +
                        "\t\treturn String(format: raw_$methodName, ${classArguments.getArgumentList(textValue.arguments)})" +
                        "\t}")
            } else {
                if (textValue.isTranslatable) {
                    builder.append("\tlet $methodName: String {\n" +
                            "\t\treturn localize(\"${textValue.getText()}\")\n" +
                            "\t}\n")
                } else {
                    builder.append("\tlet $methodName: String = \"${textValue.getText()}\"")
                }
            }
        }

        return GeneralOutput(options.outputPlatform,"",builder.toString(),true)
    }

    data class CocoaOptions(override val timeStamp: String,
                              override val outputPlatform: String,
                              override val rootLanguageId: String,

                              override val alertForMissingTexts: Boolean,
                              override val fillInMissingStrings: Boolean,
                              override val fillInMissingStringsInBlank: Boolean,

                              override val sortKeys: Boolean,

                              override val outputKeyClass: Boolean,
                              override val keyClassName: String) : Options {
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