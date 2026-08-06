package com.example.financesmanagementapp.data.local.mapper

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Record

/**
 * Extension function to convert a [Record] to a [RecordEntity].
 * From domain to data.
 * @return A [RecordEntity] object.
 */
fun Record.toEntity(): RecordEntity{
    return RecordEntity(
        accountId = accountId,
        amount = amount,
        description = description,
        isIncome = isIncome,
        category = category.category,
        subcategory = category.subcategory,
        date = date,
        currency = currency
    )
}