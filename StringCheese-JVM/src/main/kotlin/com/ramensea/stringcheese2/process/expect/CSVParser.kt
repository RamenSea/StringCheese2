package com.ramensea.stringcheese2.process.expect

import com.ramensea.stringcheese2.process.implementations.readers.ColumnParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


actual class CSVParser actual constructor(): ColumnParser {
    override fun parse(s: String): Array<Array<String>> {
        val parser = CSVParser.parse(s, CSVFormat.DEFAULT) ?: return arrayOf()
        val records = parser.records
        return Array(records.size) { i: Int ->
            val record = records[i] ?: return@Array arrayOf<String>()
            Array(record.size()) { inner: Int ->
                record[inner]
            }
        }
    }
}


actual fun getTimeStamp(): String {
    return "$TIME_STAMP_PREFIX${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}"
}