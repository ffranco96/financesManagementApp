package com.example.financesmanagementapp.data.local.mapper

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record


/**
 * Extension function to convert a [Record] to a [RecordEntity].
 * From domain to data.
 * @return A [RecordEntity] object.
 */
fun Record.toEntity(): RecordEntity{
    return RecordEntity(
        amount = amount,
        description = description,
        categoryName = category.categoryName,
        date = date,
        currency = currency
    )
}

/**
 * Extension function to convert a [RecordEntity] to a [Record].
 * From data to domain.
 * @return A [Record] object.
 */
fun RecordEntity.toDomain(): Record {
    return Record(
        amount = amount,
        description = description,
        category = Category(categoryName),
        date = date,
        currency = currency
    )
}