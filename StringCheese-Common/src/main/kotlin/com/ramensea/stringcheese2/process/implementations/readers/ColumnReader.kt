package com.ramensea.stringcheese2.process.implementations.readers

import com.ramensea.stringcheese2.models.*
import com.ramensea.stringcheese2.process.expect.CSVParser

class ColumnReader(private val parser: ColumnParser = CSVParser(),
                   private val argumentReader: ArgumentReader = StringCheeseDefaultArgumentReader()) {

    fun readString(options: Options, languageId: String, s: String): LanguageTextValueMutableSet {
        val errors = StringBuilder()
        val columns = parser.parse(s)
        val usingLanguageId: String = if (!options.firstColumnHasLanguageId) languageId else run {
            if (columns.size > 0 && columns[0].size > 0) columns[0][0]
            else languageId
        }
        val list = ArrayList<TextValue>()
        var skipFirst = options.firstColumnHasLanguageId
        for (column in columns) {
            if (skipFirst) {
                skipFirst = false
                continue
            }


            val isTranslatable: Boolean = if (!options.supportsTranslatableRow) true else run {
                if (column.size > options.rowForIsTranslatable) readBoolean(column[options.rowForIsTranslatable])
                else true
            }
            if (column.size <= options.rowForKey || column.size <= options.rowForText) throw Exception("Error processing a column, column was not wide enough to contain a key or text row")

            var key: String = column[options.rowForKey]
            var text: String = column[options.rowForText]

//            key = sanitizer.sanitizeKey(key)
            if (key == "") throw Exception("Key is a blank string after sanitizing")

            val argPlaceHolder: String = argumentReader.placeholder
            val args: Array<Argument>?

            if (argumentReader.stringHasArguments(text)) {
                val setNames: Array<String>
                if (options.supportsArgumentsNames && column.size > options.rowForArgumentNames) {
                    val argString = column[options.rowForArgumentNames]
//                    argString = sanitizer.sanitizeArgumentString(argString)
                    setNames = argString.split(",").toTypedArray()
                } else {
                    setNames = arrayOf()
                }

                var i = 0
                val argsList = ArrayList<Argument>()
                text = argumentReader.readStringForArguments(text) { index, type, decimalPlace ->
                    val name = if (setNames.size > i && setNames[i] != "") setNames[i] else "arg$i"
                    argsList.add(MutableArgument(name,type,decimalPlace))
                    i++
                    argPlaceHolder
                }
                args = argsList.toTypedArray()
            } else {
                args = null
            }
            list.add(MutableTextValue(isTranslatable,key,text,argPlaceHolder,args))
        }

        return LanguageTextValueMutableSet(usingLanguageId, list, errors)
    }

    fun readBoolean(value: String): Boolean {
        val value = value.toLowerCase()
        if (value == "no" || value == "false" || value == "0") return false
        return true
    }
    data class Options(val firstColumnHasLanguageId: Boolean = false,
                       val rowForKey: Int = 0,
                       val rowForText: Int = 1,
                       val rowForIsTranslatable: Int = 2,
                       val rowForArgumentNames: Int = 3) {
        val supportsTranslatableRow = rowForIsTranslatable >= 0
        val supportsArgumentsNames = rowForArgumentNames >= 0
    }
}



interface ColumnParser {
    fun parse(s: String): Array<Array<String>>
}