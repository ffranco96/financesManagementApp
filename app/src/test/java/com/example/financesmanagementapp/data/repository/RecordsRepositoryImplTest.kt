package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.data.local.AppDatabase
import com.example.financesmanagementapp.data.local.dao.RecordsDAO
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.FiatCurrency
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecordsRepositoryImplTest {
    private lateinit var recordsDao: RecordsDAO
    private lateinit var appDataBase: AppDatabase
    private lateinit var recordsRepository: RecordsRepository

    @Before
    fun onBefore() {
        recordsDao = mockk()
        appDataBase = mockk()
        every { appDataBase.recordsDao() } returns recordsDao
        recordsRepository = RecordsRepositoryImpl(appDataBase)
    }

    @Test
    fun `getAllRecordsFlow con registros devuelve flow con lista`() = runTest {
        val recordEntity = RecordEntity(
            amount = 1.0,
            description = "Hola mundo",
            isIncome = true,
            categoryName = Category.CATEGORY_HEALTH,
            date = "2025-01-12",
            currency = FiatCurrency.ARS.description,
        )
        every { recordsDao.getAllAsFlow() } returns flowOf(listOf(recordEntity))
        val result = recordsRepository.getAllRecordsFlow()
        assertEquals(listOf(recordEntity), result.first())
    }

    @Test
    fun `getAllRecordsFlow sin registros devuelve flow vacio`() = runTest {
        every { recordsDao.getAllAsFlow() } returns flowOf(emptyList<RecordEntity>())
        val result = recordsRepository.getAllRecordsFlow()
        assertEquals(emptyList<RecordEntity>(), result.first())
    }

    @Test
    fun `getTotalBalanceByAccountFlow delega al DAO`() = runTest {
        every { recordsDao.getTotalBalanceByAccount(any()) } returns flowOf(100.0)
        val result = recordsRepository.getTotalBalanceByAccountFlow(accountId = 1)
        assertEquals(100.0, result.first())
    }

    @Test
    fun `getBalanceByAccountAndCategoryFlow delega al DAO`() = runTest {
        every { recordsDao.getBalanceByCategoryAndAccount(any(), any()) } returns flowOf(50.0)
        val result = recordsRepository.getBalanceByAccountAndCategoryFlow(
            accountId = 1,
            categoryName = Category.CATEGORY_HEALTH
        )
        assertEquals(50.0, result.first())
    }

    @Test
    fun `addRecord delega al DAO`() = runTest {
        val record = RecordEntity(
            amount = 100.0,
            description = "Test",
            isIncome = true,
            categoryName = Category.CATEGORY_FOOD,
            date = "2025-06-01",
            currency = FiatCurrency.ARS.description
        )
        every { recordsDao.insert(any()) } just Runs
        recordsRepository.addRecord(record)
        verify { recordsDao.insert(record) }
    }

    @Test
    fun `deleteAllRecords delega al DAO`() = runTest {
        every { recordsDao.deleteAll() } just Runs
        recordsRepository.deleteAllRecords()
        verify { recordsDao.deleteAll() }
    }
}
