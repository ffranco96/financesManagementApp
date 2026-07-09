package com.example.financesmanagementapp.data.local

import com.example.financesmanagementapp.domain.model.Record

sealed class ParseError {
    data class FormatError(val field: String, val value: String) : ParseError()
    data class MissingField(val field: String) : ParseError()
    data object EmptyFile : ParseError()
}

data class CsvParseResult(
    val records: List<Record>,
    val errors: List<ParseError>
)
