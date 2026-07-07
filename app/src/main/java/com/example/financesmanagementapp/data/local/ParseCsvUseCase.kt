package com.example.financesmanagementapp.data.local

import android.content.Context
import android.util.Log
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class ParseCsvUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    operator fun invoke(inputStream: InputStream): CsvParseResult {
        val lines = inputStream
            .bufferedReader()
            .lineSequence()
            .filter { it.isNotBlank() }
            .toList()

        if (lines.isEmpty() || lines.size == 1) {
            return CsvParseResult(emptyList(), listOf(ParseError.EmptyFile))
        }

        val colIndex = parseHeader(lines.first())
        val errors = mutableListOf<ParseError>()
        val records = mutableListOf<Record>()

        for (line in lines.drop(1)) {
            when (val parsed = parseLine(line, colIndex)) {
                is ParseLineResult.Success -> records.add(parsed.record)
                is ParseLineResult.Error -> errors.add(parsed.error)
            }
        }

        return CsvParseResult(records, errors)
    }

    private fun parseHeader(header: String): Map<String, Int> {
        return header.split(";")
            .mapIndexed { i, col -> col.trim().lowercase() to i }
            .filter { (name, _) -> name.isNotBlank() }
            .toMap()
    }

    private fun parseLine(line: String, colIndex: Map<String, Int>): ParseLineResult {
        val cols = line.split(";")

        val amountIdx = colIndex["amount"]
        val categoryIdx = colIndex["categoryname"]
        val dateIdx = colIndex["date"]
        val currencyIdx = colIndex["currency"]
        val descriptionIdx = colIndex["description"]

        if (amountIdx == null) return ParseLineResult.Error(ParseError.MissingField("amount"))
        if (categoryIdx == null) return ParseLineResult.Error(ParseError.MissingField("categoryName"))
        if (dateIdx == null) return ParseLineResult.Error(ParseError.MissingField("date"))
        if (currencyIdx == null) return ParseLineResult.Error(ParseError.MissingField("currency"))

        val amountStr = cols.getOrNull(amountIdx)?.trim()
        val categoryName = cols.getOrNull(categoryIdx)?.trim()
        val date = cols.getOrNull(dateIdx)?.trim()
        val currency = cols.getOrNull(currencyIdx)?.trim()
        val description = descriptionIdx?.let { cols.getOrNull(it)?.trim() } ?: ""

        if (amountStr.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("amount"))
        if (categoryName.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("categoryName"))
        if (date.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("date"))
        if (currency.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("currency"))

        val amount = try {
            amountStr.toDouble()
        } catch (e: NumberFormatException) {
            return ParseLineResult.Error(ParseError.FormatError("amount", amountStr))
        }

        val category = Category.fromName(categoryName)
        logResource(category)

        return ParseLineResult.Success(
            Record(
                accountId = Record.DEFAULT_ACCOUNT_ID,
                amount = amount,
                description = description,
                category = category,
                date = date,
                currency = currency
            )
        )
    }

    private fun logResource(category: Category) {
        try {
            val resourceName = context.resources.getResourceEntryName(category.iconRsc)
            Log.d("CheckResource", "El recurso para ${category.categoryName} es: $resourceName")
        } catch (e: Exception) {
            Log.e("CheckResource", "Error al obtener nombre del recurso para ${category.categoryName}")
        }
    }

    private sealed class ParseLineResult {
        data class Success(val record: Record) : ParseLineResult()
        data class Error(val error: ParseError) : ParseLineResult()
    }
}
