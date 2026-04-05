package com.example.financesmanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

/**
 * Entity representing a financial record in the local database.
 * Matches the structure of the [com.example.financesmanagementapp.ui.Record] domain class.
 */
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val description: String,
    val isIncome: Boolean,
    val categoryName: String, // Flattening Category for simplicity
    val date: String,
    val currency: String
): Comparable<RecordEntity>{
    override fun compareTo(other: RecordEntity): Int {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1 = formatter.parse(date)
        val date2 = formatter.parse(other.date)
        return date1.compareTo(date2)
    }
}
