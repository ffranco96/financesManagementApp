package com.example.financesmanagementapp.ui.addrecordetail.domain

import com.example.financesmanagementapp.data.local.mapper.toEntity
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Record
import javax.inject.Inject

/**
 * Use case for updating an existing financial record.
 *
 * @property recordsRepository The repository for managing financial records.
 */
class UpdateRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
) {
    suspend operator fun invoke(record: Record) {
        val recordWithAccount = record.copy(accountId = Record.DEFAULT_ACCOUNT_ID)
        recordsRepository.updateRecord(recordWithAccount.toEntity())
    }
}
