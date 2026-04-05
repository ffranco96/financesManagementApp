package com.example.financesmanagementapp.ui.addrecordetail.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.ui.Record
import com.example.financesmanagementapp.ui.toEntity
import javax.inject.Inject

/**
 * Use case for saving a financial record.
 *
 * @property recordsRepository The repository for managing financial records.
 */
class SaveRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    suspend operator fun invoke(record: Record){
        recordsRepository.addRecord(record.toEntity())
    }
}