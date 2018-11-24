package com.ramensea.stringcheese2.process.implementations.generators.cocoa

import com.ramensea.stringcheese2.models.Argument
import com.ramensea.stringcheese2.models.ClassArgumentWriter
import com.ramensea.stringcheese2.process.implementations.generators.StringSanitizer


class CocoaTouchKeyClassArguments(override val sanitizer: StringSanitizer): ClassArgumentWriter {
    override fun getFormatString(index: Int): String = "%@"
    override fun getFormatInt(index: Int): String = "%d"
    override fun getFormatLong(index: Int): String = "%ld"
    override fun getFormatFloat(index: Int, decimalPlace: Int): String = if (decimalPlace < 0) "%f" else "%.${decimalPlace}f"

    override fun getArgumentTypeName(type: Argument.Types): String {
        return when (type) {
            Argument.Types.STRING -> "String"
            Argument.Types.INT -> "Int"
            Argument.Types.LONG -> "Int"
            Argument.Types.FLOAT -> "Float"
        }
    }

    override fun getArgumentList(arguments: Array<Argument>): String {
        val builder = StringBuilder()
        var isFirst = true
        for (argument in arguments) {
            builder.append("${if (!isFirst)", " else ""}${argument.name}: ${getArgumentTypeName(argument.type)}")
            isFirst = false
        }
        return builder.toString()
    }

    override fun getArgumentListForFormat(arguments: Array<Argument>): String {
        val builder = StringBuilder()
        var isFirst = true
        for (argument in arguments) {
            builder.append("${if (!isFirst)", " else ""}${argument.name}")
            isFirst = false
        }
        return builder.toString()
    }
}