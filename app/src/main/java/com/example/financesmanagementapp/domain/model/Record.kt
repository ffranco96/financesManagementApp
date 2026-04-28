package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Data class representing a financial record, which is a movement that can be an income or an expense.
 */
data class Record (
    var accountId: Int = 0,
    var amount: Double = 0.0,
    var description: String = "",
    var isIncome: Boolean = false,
    var category: Category = Category(),
    var date:String = "", // Format yyyy-MM-dd
    var currency:String = "",
): Serializable, Comparable<Record> {
    /**
     * Compares this record with other based on the date field (ISO yyyy-MM-dd)
     * Allows chronological sorting of records. If date has no valid format, compares
     * alphabetically.
     */
    override fun compareTo(other: Record): Int {
        return try {
            val date1 = LocalDate.parse(this.date)
            val date2 = LocalDate.parse(other.date)
            date1.compareTo(date2)
        } catch (e: DateTimeParseException) {
            this.date.compareTo(other.date)
        }
    }

    companion object {
        const val DEFAULT_ACCOUNT_ID = 0
    }
}

/**
 * Extension function to convert a [Record] domain class to a [RecordEntity] entity.
 */
fun Record.toEntity(): RecordEntity {
    return RecordEntity(
        accountId = accountId,
        amount = amount,
        description = description,
        isIncome = isIncome,
        categoryName = category.categoryName,
        date = date,
        currency = currency
    )
}