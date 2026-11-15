package com.example.financesmanagementapp.data.local

import android.net.Uri
import com.example.financesmanagementapp.domain.crash.CrashReporter
import com.example.financesmanagementapp.domain.model.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Use case to export a list of [Record]s to a CSV document chosen by the user.
 *
 * Generates CSV content (semicolon-delimited) and delegates the file write
 * to [CsvFileWriter]. The suggested file name is auto-generated with the
 * current date-time in `records_yyyyMMdd_HHmmss.csv` format.
 *
 * @param csvFileWriter Abstraction for file I/O, injected by Hilt.
 * @param crashReporter Reporter used to log unexpected export failures.
 */
class ExportCsvUseCase @Inject constructor(
    private val csvFileWriter: CsvFileWriter,
    private val crashReporter: CrashReporter
) {
    /**
     * Exports [records] to the document identified by [destination].
     *
     * @param records The list of records to export.
     * @param destination The destination document URI returned by the system
     * file picker (`ActivityResultContracts.CreateDocument`).
     * @return `true` if the export succeeded, `false` if an error occurred.
     */
    suspend operator fun invoke(
        records: List<Record>,
        destination: Uri
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val csvContent = buildCsvContent(records)
            csvFileWriter.writeCsv(destination, csvContent)
        } catch (e: Exception) {
            crashReporter.recordException(e, "CSV export failed")
            false
        }
    }

    /**
     * Builds the full CSV content string from the given [records].
     *
     * The output starts with the header line (`amount; description; category; subcategory; date; currency`)
     * followed by one line per record, using `;` as delimiter.
     *
     * @param records The records to serialize.
     * @return The complete CSV string.
     */
    internal fun buildCsvContent(records: List<Record>): String {
        val sb = StringBuilder()
        sb.appendLine("amount; description; category; subcategory; date; currency")
        records.forEach { record ->
            sb.appendLine("${record.amount}; ${record.description}; ${record.category.categoryName}; ${record.category.subcategoryName}; ${record.date}; ${record.currency}")
        }
        return sb.toString()
    }

    /**
     * Generates the file name suggested to the user in the system file picker.
     *
     * Format: `records_yyyyMMdd_HHmmss.csv`
     *
     * @return A unique file name string.
     */
    internal fun suggestedFileName(): String {
        val formattedTime = LocalDateTime.now().format(FILE_NAME_FORMATTER)
        return "records_${formattedTime}.csv"
    }

    private companion object {
        val FILE_NAME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    }
}
