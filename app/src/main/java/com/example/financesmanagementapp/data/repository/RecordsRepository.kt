package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing financial records in the application.
 */
interface RecordsRepository {

    /**
     * Retrieves all records from the database as a Flow.
     * At this point, the category attribute is as a simple String,
     * not a Category object.
     * @return A flow emitting the list of records whenever the database changes.
     */
    fun getAllRecordsFlow(): Flow<List<RecordEntity>>

    /**
     * Retrieves the total balance for a specific account as a Flow.
     * @param accountId The ID of the account.
     * @return A flow emitting the total balance whenever the database changes (insertion or
     * update of records).
     */
    fun getTotalBalanceByAccountFlow(accountId: Int): Flow<Double?>

    /**
     * Retrieves the balance for a specific account and category as a Flow.
     * @param accountId The ID of the account.
     * @param categoryName The name of the category.
     * @return A flow emitting the balance whenever the database changes (insertion or
     * update of records).
     */
    fun getBalanceByAccountAndCategoryFlow(accountId: Int, categoryName: String): Flow<Double?>

    /**
     * Adds and persists a new financial record to the database.
     * @param record The entity representing the record to be saved.
     * @return The ID of the inserted record.
     */
    suspend fun addRecord(record: RecordEntity)

    /**
     * Deletes all financial records from the database.
     */
    suspend fun deleteAllRecords()
}
