package com.example.financesmanagementapp.domain.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * Data class representing a financial record, which is a movement that can be an income or an expense.
 */
data class Record (
    var accountId: Int = 0,
    var amount: Double = 0.0,
    var description: String = "",
    var category: Category = Category(),
    var date:String = "", // Format yyyy-MM-dd'T'HH:mm:ss (ISO 8601) or yyyy-MM-dd for legacy records
    var currency:String = "",
): Serializable, Comparable<Record> {
    /**
     * Compares this record with other based on the date field.
     * Tries [LocalDateTime] first (ISO 8601 with time), falls back to [LocalDate],
     * then to string comparison.
     */
    override fun compareTo(other: Record): Int {
        try {
            val date1 = LocalDateTime.parse(this.date)
            val date2 = LocalDateTime.parse(other.date)
            return date1.compareTo(date2)
        } catch (_: DateTimeParseException) {} // To manage parsing errors in legacy records
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