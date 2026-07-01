package com.example.financesmanagementapp.data.local

import android.content.Context
import android.util.Log
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

/**
 * Use case for parsing a CSV file and returning a list of [Record] objects.
 */
class ParseCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
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
        // CSV structure: amount[0]; description[1]; categoryName[2]; date[3]; currency[4]
        
        val category = Category.fromName(columns[2].trim())

        try {
            val resourceName = context.resources.getResourceEntryName(category.iconRsc)
            Log.d("CheckResource", "El recurso para ${category.categoryName} es: $resourceName")
        } catch (e: Exception) {
            Log.e("CheckResource", "Error al obtener nombre del recurso para ${category.categoryName}")
        }

        return Record(
            accountId = Record.DEFAULT_ACCOUNT_ID,
            amount = columns[0].trim().toDouble(),
            description = columns[1].trim(),
            category = category,
            date = columns[3].trim(),
            currency = columns[4].trim()
        )
    }
}
