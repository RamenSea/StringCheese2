package com.ramensea.stringcheese2.models

import com.ramensea.stringcheese2.process.implementations.generators.StringSanitizer

interface TextValue {
    val isTranslatable: Boolean
    val key: String
    val rawText: String
    val argumentPlaceholderRegex: String
    val arguments: Array<Argument>?
}
//
//fun TextValue.getText(argumentWriter: ClassArgumentWriter? = null): String {
//    if (numberOfArguments() == 0 || argumentWriter == null) return rawText
//    return argumentWriter.writeArgumentText(this)
//}
fun TextValue.numberOfArguments(): Int = arguments?.size ?: 0
fun TextValue.hasArguments(): Boolean = numberOfArguments() > 0

fun TextValue.copy(shouldEmptyText: Boolean): TextValue = MutableTextValue(isTranslatable, key, if (shouldEmptyText) "" else rawText, argumentPlaceholderRegex, arguments)

data class MutableTextValue(override var isTranslatable: Boolean,
                            override var key: String,
                            override var rawText: String,
                            override var argumentPlaceholderRegex: String = "",

                            override var arguments: Array<Argument>? = null): TextValue

interface Argument {
    val name: String
    val type: Types
    val decimalPlace: Int
    enum class Types {
        STRING, INT, LONG, FLOAT
    }
}

data class MutableArgument(override var name: String,
                           override var type: Argument.Types,
                           override var decimalPlace: Int): Argument {


}

interface ArgumentReader {
    val placeholder: String

    val stringHolder: String
    val intHolder: String
    val longHolder: String
    val floatHolder: String

    val escapeChar: String
    val regexEscapedEscapeChar: String
    private fun getFindArgumentsPlusEscapedRegex(): String = "($regexEscapedEscapeChar$stringHolder|$stringHolder|$regexEscapedEscapeChar$intHolder|$intHolder|$regexEscapedEscapeChar$longHolder|$longHolder|$regexEscapedEscapeChar$floatHolder|$floatHolder)"
    fun stringHasArguments(s: String): Boolean {
        val regex = getFindArgumentsPlusEscapedRegex().toRegex()
        val matches = regex.findAll(s)
        matches.forEach {
            if (!isArgumentEscaped(it.value)) return true
        }
        return false
    }
    fun readStringForArguments(s: String, next: (index: Int, type: Argument.Types, decimalPlace: Int) -> String): String {
        var i = 0
        return s.replace(getFindArgumentsPlusEscapedRegex().toRegex()) { matchResult ->
            if (!isArgumentEscaped(matchResult.value)) {
                val setTo = next(i, getType(matchResult.value),-1) //todo add decimal place
                i++
                return@replace setTo
            }
            return@replace matchResult.value
        }
    }
    fun isArgumentEscaped(s: String): Boolean {
        return when (s) {
            escapeChar + stringHolder,
            escapeChar + intHolder,
            escapeChar + longHolder,
            escapeChar + floatHolder -> true
            else -> false
        }
    }
    fun getType(holder: String): Argument.Types {
        return when(holder) {
            stringHolder,placeholder -> Argument.Types.STRING
            intHolder -> Argument.Types.INT
            longHolder -> Argument.Types.LONG
            floatHolder -> Argument.Types.FLOAT
            else -> Argument.Types.STRING
        }
    }
}
class StringCheeseDefaultArgumentReader: ArgumentReader {
    override val placeholder: String = "%s"
    override val stringHolder: String = "%s"
    override val intHolder: String = "%i"
    override val longHolder: String = "%l"
    override val floatHolder: String = "%f"


    override val escapeChar: String = "\\"
    override val regexEscapedEscapeChar: String = "\\\\"
}

interface ClassArgumentWriter {
    val sanitizer: StringSanitizer

    fun getFormat(type: Argument.Types, index: Int, decimalPlace: Int = -1): String {
        return when (type) {
            Argument.Types.STRING -> getFormatString(index)
            Argument.Types.INT -> getFormatInt(index)
            Argument.Types.LONG -> getFormatLong(index)
            Argument.Types.FLOAT -> getFormatFloat(index)
        }
    }
    fun getFormatString(index: Int): String
    fun getFormatInt(index: Int): String
    fun getFormatLong(index: Int): String
    fun getFormatFloat(index: Int, decimalPlace: Int = -1): String

    fun getArgumentTypeName(type: Argument.Types): String
    fun getArgumentList(arguments: Array<Argument>): String
    fun getArgumentListForFormat(arguments: Array<Argument>): String

    fun writeArgumentText(argumentReader: ArgumentReader, textValue: TextValue): String {
        val arguments = textValue.arguments ?: return textValue.rawText
        if (!textValue.hasArguments()) return textValue.rawText


        val escapedPlaceholder = "${argumentReader.escapeChar}${argumentReader.placeholder}"
        val regex = "(${argumentReader.regexEscapedEscapeChar}${argumentReader.placeholder}|${argumentReader.placeholder})".toRegex()
        var i = 0
        return sanitizer.sanitizeText(textValue.rawText).replace(regex) { matchResult ->
            if (matchResult.value == escapedPlaceholder) return@replace matchResult.value
            var replace = ""
            if (i < arguments.size) {
                val arg = arguments[i]
                replace = getFormat(arg.type,i,arg.decimalPlace)
            }
            i++
            replace
        }
    }
}