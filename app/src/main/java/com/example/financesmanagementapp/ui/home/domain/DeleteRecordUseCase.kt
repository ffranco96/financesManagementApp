package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import javax.inject.Inject

/**
 * Use case for deleting a single record from the repository.
 */
class DeleteRecordUseCase @Inject constructor(
    private val recordsRecordsRepository: RecordsRepository
){
    suspend operator fun invoke(id: Int) {
        recordsRecordsRepository.deleteRecordById(id)
    }
}
