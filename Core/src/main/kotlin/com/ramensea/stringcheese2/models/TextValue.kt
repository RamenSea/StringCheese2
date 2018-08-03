package com.ramensea.stringcheese2.models

data class TextValue(val isTranslatable: Boolean,
                     val key: String,
                     private val text: String,
                     private val argumentPlaceholder: String = "",

                     val arguments: Array<Argument>? = null) {

    fun getText(argumentWriter: ClassArgumentWriter? = null): String {
        if (numberOfArguments() == 0 || argumentWriter == null) return text
        return getArgumentString(argumentWriter)
    }
    fun numberOfArguments(): Int = arguments?.size ?: 0
    fun hasArguments(): Boolean = numberOfArguments() > 0


    fun getArgumentString(argumentWriter: ClassArgumentWriter): String {
        if (numberOfArguments() == 0 || arguments == null) return text

        var i = 0
        val argumentText = text.replace(argumentPlaceholder.toRegex()) { matchResult ->
            var replace = ""
            if (i < arguments.size) {
                val arg = arguments[i]
                replace = argumentWriter.getFormat(arg.type,i,arg.decimalPlace)
            }
            i++
            replace
        }

        return argumentText
    }
    fun copy(shouldEmptyText: Boolean): TextValue = TextValue(isTranslatable, key, if (shouldEmptyText) "" else text, argumentPlaceholder, arguments)
}

data class Argument(val name: String,
                    val type: Types,
                    val decimalPlace: Int) {

    enum class Types {
        STRING, INT, LONG, FLOAT, DOUBLE
    }
}

interface ArgumentReader {
    val placeholder: String

    val stringRegex: String
    val intRegex: String
    val longRegex: String
    val floatRegex: String
    val doubleRegex: String
}
class StringCheeseDefaultArgumentReader: ArgumentReader {
    override val placeholder: String = "%s"
    override val stringRegex: String = "%s"
    override val intRegex: String = "%i"
    override val longRegex: String = "%l"
    override val floatRegex: String = "%f"
    override val doubleRegex: String = "%d"
}

interface ClassArgumentWriter {
    fun getFormat(type: Argument.Types, index: Int, decimalPlace: Int = -1): String {
        return when (type) {
            Argument.Types.STRING -> getFormatString(index)
            Argument.Types.INT -> getFormatInt(index)
            Argument.Types.LONG -> getFormatLong(index)
            Argument.Types.FLOAT -> getFormatFloat(index)
            Argument.Types.DOUBLE -> getFormatDouble(index)
        }
    }
    fun getFormatString(index: Int): String
    fun getFormatInt(index: Int): String
    fun getFormatLong(index: Int): String
    fun getFormatFloat(index: Int, decimalPlace: Int = -1): String
    fun getFormatDouble(index: Int, decimalPlace: Int = -1): String

    fun getArgumentTypeName(type: Argument.Types): String
    fun getArgumentList(arguments: Array<Argument>): String
    fun getArgumentListForFormat(arguments: Array<Argument>): String
}