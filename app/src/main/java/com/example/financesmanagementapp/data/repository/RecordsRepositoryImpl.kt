package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.data.local.AppDatabase
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.Int

/**
 * Implementation of [RecordsRepository] that uses Room database as the data source.
 *
 * @property appDatabase The Room database instance providing access to DAOs.
 */
class RecordsRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : RecordsRepository {

    private val recordsDao = appDatabase.recordsDao()

    /**
     * Retrieves all records from the database as a Flow.
     * At this point, the category attribute is as a simple String,
     * not a Category object.
     */
    override fun getAllRecordsFlow(): Flow<List<RecordEntity>> {
        return recordsDao.getAllAsFlow()
    }

    override fun getTotalBalanceByAccountFlow(accountId: Int): Flow<Double?> {
        return recordsDao.getTotalBalanceByAccount()
    }

    override fun getBalanceByAccountAndCategoryFlow(accountId: Int, categoryName: String): Flow<Double?> {
        return recordsDao.getBalanceByCategoryAndAccount(accountId, categoryName)
    }

    /**
     * Persists a financial record into the local database.
     *
     * @param record The entity representing the record to be saved.
     */
    override suspend fun addRecord(record: RecordEntity) {
        recordsDao.insert(record)
    }

    /**
     * Removes all records from the financial records table.
     */
    override suspend fun deleteAllRecords() {
        recordsDao.deleteAll()
    }
}
