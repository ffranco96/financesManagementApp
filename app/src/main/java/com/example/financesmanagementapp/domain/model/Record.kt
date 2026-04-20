package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import java.io.Serializable

/**
 * Data class representing a financial record, which is a movement that can be an income or an expense.
 */
data class Record (
    var amount: Double = 0.0,
    var description: String = "",
    var isIncome: Boolean = false,
    var category: Category = Category(),
    var date:String = "",
    var currency:String = ""
): Serializable

/**
 * Extension function to convert a [Record] domain class to a [RecordEntity] entity.
 */
fun Record.toEntity(): RecordEntity {
    return RecordEntity(
        amount = amount,
        description = description,
        isIncome = isIncome,
        categoryName = category.categoryName,
        date = date,
        currency = currency
    )
}