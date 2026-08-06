package com.example.financesmanagementapp.data.local

import com.example.financesmanagementapp.domain.model.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Use case to export a list of [Record]s to a CSV file.
 *
 * Generates CSV content (semicolon-delimited) and delegates the file write
 * to [CsvFileWriter]. The file name is auto-generated with the current
 * date-time in `records_yyyyMMdd_HHmmss.csv` format.
 *
 * @param csvFileWriter Abstraction for file I/O, injected by Hilt.
 */
class ExportCsvUseCase @Inject constructor(
    private val csvFileWriter: CsvFileWriter
) {
    /**
     * Exports [records] to a CSV file.
     *
     * @param records The list of records to export.
     * @return `true` if the export succeeded, `false` if an error occurred.
     */
    suspend operator fun invoke(records: List<Record>): Boolean = withContext(Dispatchers.IO) {
        try {
            val csvContent = buildCsvContent(records)
            val fileName = generateFileNameAutomaticallyWithDateTime()
            csvFileWriter.writeCsv(csvContent, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Builds the full CSV content string from the given [records].
     *
     * The output starts with the header line (`amount; description; categoryName; date; currency`)
     * followed by one line per record, using `;` as delimiter.
     *
     * @param records The records to serialize.
     * @return The complete CSV string.
     */
    internal fun buildCsvContent(records: List<Record>): String {
        val sb = StringBuilder()
        sb.appendLine("amount; description; isIncome; category; subcategory; date; currency")
        records.forEach { record ->
            sb.appendLine("${record.amount}; ${record.description}; ${record.category.categoryName}; ${record.date}; ${record.currency}")
        }
        return sb.toString()
    }

    /**
     * Generates a file name based on the current date-time.
     *
     * Format: `records_yyyyMMdd_HHmmss.csv`
     *
     * @return A unique file name string.
     */
    private fun generateFileNameAutomaticallyWithDateTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val formattedTime = currentTime.format(formatter)
        return "records_${formattedTime}.csv"
    }
}
