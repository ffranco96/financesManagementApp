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

    override fun getAllRecordsFlow(): Flow<List<RecordEntity>> {
        return recordsDao.getAllAsFlow()
    }

    override fun getTotalBalanceByAccountFlow(accountId: Int): Flow<Double?> {
        return recordsDao.getTotalBalanceByAccount()
    }

    override fun getBalanceByAccountAndCategoryFlow(accountId: Int, categoryName: String): Flow<Double?> {
        return recordsDao.getBalanceByCategoryAndAccount(accountId, categoryName)
    }

    override suspend fun addRecord(record: RecordEntity) {
        recordsDao.insert(record)
    }

    override suspend fun deleteAllRecords() {
        recordsDao.deleteAll()
    }
}
