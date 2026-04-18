package com.example.financesmanagementapp.data.local

import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import java.io.InputStream
import javax.inject.Inject

/**
 * Use case for parsing a CSV file and returning a list of [Record] objects.
 */
class ParseCsvUseCase @Inject constructor() {
    operator fun invoke(inputStream: InputStream): List<Record> {
        return inputStream
            .bufferedReader()
            .lineSequence()
            .drop(1)                          // Skip header
            .filter { it.isNotBlank() }
            .map { line -> parseLine(line) }
            .toList()
    }

    private fun parseLine(line: String): Record {
        val columns = line.split(";")
        return Record(
            amount = columns[0].trim().toDouble(),
            description = columns[1].trim(),
            category = Category(columns[2].trim()),
            date = columns[3].trim(),
            currency = columns[4].trim(),
            isIncome = columns[5].trim().toBoolean()
        )
    }
}