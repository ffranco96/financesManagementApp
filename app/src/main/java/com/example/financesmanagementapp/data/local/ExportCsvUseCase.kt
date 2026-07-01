package com.example.financesmanagementapp.data.local

import android.content.Context
import androidx.core.net.toUri
import com.example.financesmanagementapp.domain.model.Record
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Use case to export a list of records to a CSV file.
 */
class ExportCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Writes the list of records to the provided URI in CSV format.
     */
    suspend operator fun invoke(records: List<Record>): Boolean = withContext(Dispatchers.IO) {
        try {
            val directory = context.filesDir
            val fileName = generateFileNameAutomaticallyWithDateTime()
            val file = File(directory, fileName)
            if (file.exists()){
                throw FileAlreadyExistsException(file)
            }

            context.contentResolver.openOutputStream(file.toUri())?.use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    // Header
                    writer.write("amount; description; categoryName; date; currency")
                    writer.newLine()
                    
                    // Records
                    records.forEach { record ->
                        val line = "${record.amount}; ${record.description}; ${record.category.categoryName}; ${record.date}; ${record.currency}"
                        writer.write(line)
                        writer.newLine()
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun generateFileNameAutomaticallyWithDateTime(): String{
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val formattedTime = currentTime.format(formatter)

        return "records_${formattedTime}.csv"
    }
}
