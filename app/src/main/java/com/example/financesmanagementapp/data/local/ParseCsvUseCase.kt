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

    /**
     * Parses a single line of a CSV file.
     * CSV structure: amount[0]; description[1]; isIncome[2]; category[3]; subcategory[4]; date[5]; currency[6]
     *
     * @param line The line to parse.
     * @param columnIndex A map of column names to their corresponding indices.
     *
     * @return A [ParseLineResult] indicating success or failure.
     */
    private fun parseLine(line: String, columnIndex: Map<String, Int>): ParseLineResult {
        val cols = line.split(";")

        val amountIdx = columnIndex["amount"]
        val categoryNameIdx = columnIndex["category"]
        val subcategoryNameIdx = columnIndex["subcategory"]
        val dateIdx = columnIndex["date"]
        val currencyIdx = columnIndex["currency"]
        val descriptionIdx = columnIndex["description"]

        if (amountIdx == null) return ParseLineResult.Error(ParseError.MissingField("amount"))
        if (categoryNameIdx == null) return ParseLineResult.Error(ParseError.MissingField("category"))
        if (subcategoryNameIdx == null) return ParseLineResult.Error(ParseError.MissingField("subcategory"))
        if (dateIdx == null) return ParseLineResult.Error(ParseError.MissingField("date"))
        if (currencyIdx == null) return ParseLineResult.Error(ParseError.MissingField("currency"))

        val amountStr = cols.getOrNull(amountIdx)?.trim()
        val categoryName = cols.getOrNull(categoryNameIdx)?.trim()
        val subCategoryName = cols.getOrNull(subcategoryNameIdx)?.trim()
        val date = cols.getOrNull(dateIdx)?.trim()
        val currency = cols.getOrNull(currencyIdx)?.trim()
        val description = descriptionIdx?.let { cols.getOrNull(it)?.trim() } ?: ""

        if (amountStr.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("amount"))
        if (categoryName.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("categoryName"))
        if (subCategoryName.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("subCategoryName"))
        if (date.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("date"))
        if (currency.isNullOrBlank()) return ParseLineResult.Error(ParseError.MissingField("currency"))

        val amount = try {
            amountStr.toDouble()
        } catch (e: NumberFormatException) {
            return ParseLineResult.Error(ParseError.FormatError("amount", amountStr))
        }

        val category = Category.fromCategoryAndSubcategory(categoryName, subCategoryName)
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
            Log.d("CheckResource", "El recurso para ${category.displayLabel} es: $resourceName")
        } catch (e: Exception) {
            Log.e("CheckResource", "Error al obtener nombre del recurso para ${category.displayLabel}")
        }
    }

    private sealed class ParseLineResult {
        data class Success(val record: Record) : ParseLineResult()
        data class Error(val error: ParseError) : ParseLineResult()
    }
}
