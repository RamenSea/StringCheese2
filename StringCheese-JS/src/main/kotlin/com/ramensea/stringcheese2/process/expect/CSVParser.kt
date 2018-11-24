package com.ramensea.stringcheese2.process.expect

import com.ramensea.stringcheese2.process.implementations.readers.ColumnParser


actual class CSVParser actual constructor(): ColumnParser {
    override fun parse(s: String): Array<Array<String>> {
        val data = Papa.parse(s)
        return data.data
    }
}


external object Papa {
    fun parse(s: String): PapaCSVResult
    fun unparse(csv: Array<Array<String>>): String
}

external class PapaCSVResult {
    val data: Array<Array<String>>
}
