package com.example.financesmanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.Record.Companion.DEFAULT_ACCOUNT_ID
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * Entity representing a financial record in the local database.
 * Matches the structure of the [Record] domain class.
 */
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ACCOUNT_ID,
    val accountId: Int = 0,
    val amount: Double,
    val description: String,
    val category: String, // Flattening Category for simplicity
    val subcategory: String,
    val date: String, // Format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd for legacy
    val currency: String
): Comparable<RecordEntity>{
    override fun compareTo(other: RecordEntity): Int {
        try {
            val date1 = LocalDateTime.parse(date)
            val date2 = LocalDateTime.parse(other.date)
            return date1.compareTo(date2)
        } catch (_: DateTimeParseException) { }
        return try {
            val date1 = LocalDate.parse(date)
            val date2 = LocalDate.parse(other.date)
            date1.compareTo(date2)
        } catch (_: DateTimeParseException) {
            date.compareTo(other.date)
        }
    }
}

