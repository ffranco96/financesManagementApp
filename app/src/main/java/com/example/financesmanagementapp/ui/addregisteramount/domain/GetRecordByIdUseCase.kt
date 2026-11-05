package com.example.financesmanagementapp.ui.addregisteramount.domain

import com.example.financesmanagementapp.data.local.mapper.toDomain
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import javax.inject.Inject

/**
 * Use case to retrieve a single financial record by its id.
 *
 * @property recordsRepository The repository to fetch records from.
 */
class GetRecordByIdUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    /**
     * Executes the use case to get the record with the given [id] as a [Record].
     * Uses [Category.fromCategoryAndSubcategory] so the icons and colors are always correct
     * and up to date with the current build resources.
     *
     * @param id The id of the record to retrieve.
     * @return The matching [Record], or null if no record has that id.
     */
    suspend operator fun invoke(id: Int): Record? {
        val entity = recordsRepository.getRecordById(id) ?: return null
        val category = Category.fromCategoryAndSubcategory(entity.category, entity.subcategory)
        return entity.toDomain(category)
    }
}
