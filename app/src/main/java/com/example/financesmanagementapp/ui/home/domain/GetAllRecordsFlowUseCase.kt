package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.data.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve all financial records as a Flow.
 *
 * @property recordsRepository The repository to fetch records from.
 */
class GetAllRecordsFlowUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository
) {
    /**
     * Executes the use case to get the flow of all records.
     *
     * @return A Flow emitting the list of records.
     */
    operator fun invoke(): Flow<List<RecordEntity>> {
        return recordsRepository.getAllRecordsFlow()
    }
}
