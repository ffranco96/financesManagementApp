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