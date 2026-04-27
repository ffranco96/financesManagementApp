package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing financial records in the application.
 */
interface RecordsRepository {

    /**
     * Retrieves all records from the database as a Flow.
     * @return A flow emitting the list of records whenever the database changes.
     */
    fun getAllRecordsFlow(): Flow<List<RecordEntity>>

    fun getTotalBalanceByAccountFlow(accountId: Int): Flow<Double?>

    fun getBalanceByAccountAndCategoryFlow(accountId: Int, categoryName: String): Flow<Double?>

    /**
     * Adds a new financial record to the database.
     * @param record The record entity to be added.
     */
    suspend fun addRecord(record: RecordEntity)

    /**
     * Deletes all financial records from the database.
     */
    suspend fun deleteAllRecords()
}
