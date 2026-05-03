package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.local.entities.toDomain
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to retrieve all financial records as a Flow.
 *
 * @property recordsRepository The repository to fetch records from.
 */
class GetAllRecordsFlowUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    /**
     * Executes the use case to get the flow of all records as [Record] objects.
     * Use Category.fromName to ensure the icons and colors are always correct 
     * and up to date with the current build resources.
     *
     * @return A Flow emitting the list of records with the complete category as a [List<Record>].
     */
    operator fun invoke(): Flow<List<Record>> {
        return recordsRepository.getAllRecordsFlow().map { entities ->
            entities.map { recordEntity ->
                val category = Category.fromName(recordEntity.categoryName)
                recordEntity.toDomain(category)
            }
        }
    }
}
