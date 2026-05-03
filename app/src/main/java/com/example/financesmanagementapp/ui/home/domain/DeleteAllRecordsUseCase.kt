package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import javax.inject.Inject

/**
 * Use case for deleting all records from the repository.
 * @property recordsRecordsRepository The repository for managing financial records.
 */
class DeleteAllRecordsUseCase @Inject constructor(
    private val recordsRecordsRepository: RecordsRepository
){
    suspend operator fun invoke() {
        recordsRecordsRepository.deleteAllRecords()
    }
}