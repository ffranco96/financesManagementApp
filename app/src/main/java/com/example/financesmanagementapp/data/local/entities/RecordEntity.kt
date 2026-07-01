package com.example.financesmanagementapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.Record.Companion.DEFAULT_ACCOUNT_ID
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
    val categoryName: String, // Flattening Category for simplicity
    val date: String, // Format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd for legacy
    val currency: String
): Comparable<RecordEntity>{
    override fun compareTo(other: RecordEntity): Int {
        try {
            val date1 = java.time.LocalDateTime.parse(date)
            val date2 = java.time.LocalDateTime.parse(other.date)
            return date1.compareTo(date2)
        } catch (_: Exception) { }
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd")
        val date1 = formatter.parse(date)
        val date2 = formatter.parse(other.date)
        return date1.compareTo(date2)
    }
}

/**
 * Extension function to convert a [RecordEntity] to a [Record] domain class.
 * @param completeCategory The complete object of type [Category] category associated with the
 * record, obtained from the data store in run-time. Will be assigned to the 'category' attribute.
 */
fun RecordEntity.toDomain(completeCategory: Category): Record {
    return Record(
        accountId = accountId,
        amount = amount,
        description = description,
        category = completeCategory,
        date = date,
        currency = currency
    )
}

