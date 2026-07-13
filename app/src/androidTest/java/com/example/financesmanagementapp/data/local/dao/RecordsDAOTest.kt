package com.example.financesmanagementapp.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.financesmanagementapp.data.local.AppDatabase
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordsDAOTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: RecordsDAO

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.recordsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // ── Helper ──────────────────────────────────────────────────────────

    private fun buildRecord(
        id: Int = 0,
        accountId: Int = 0,
        amount: Double = 100.0,
        description: String = "Registro de prueba",
        categoryName: String = "Comida y alimentos",
        date: String = "2026-01-15T10:30:00",
        currency: String = "ARS"
    ) = RecordEntity(
        id = id,
        accountId = accountId,
        amount = amount,
        description = description,
        categoryName = categoryName,
        date = date,
        currency = currency
    )

    // ── getAll ───────────────────────────────────────────────────────────

    @Test
    fun givenEmptyDatabase_whenGetAll_thenReturnsEmptyList() = runTest {
        val result = dao.getAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun givenRecordsInserted_whenGetAll_thenReturnsAllRecords() = runTest {
        val record1 = buildRecord(description = "Record 1", amount = 50.0)
        val record2 = buildRecord(description = "Record 2", amount = -30.0)
        dao.insert(record1, record2)

        val result = dao.getAll()
        assertEquals(2, result.size)
    }

    // ── getAllAsFlow ─────────────────────────────────────────────────────

    @Test
    fun givenEmptyDatabase_whenGetAllAsFlow_thenEmitsEmptyList() = runTest {
        val result = dao.getAllAsFlow().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun givenRecordsInserted_whenGetAllAsFlow_thenEmitsAllRecords() = runTest {
        val record = buildRecord(description = "Flow record")
        dao.insert(record)

        val result = dao.getAllAsFlow().first()
        assertEquals(1, result.size)
        assertEquals("Flow record", result[0].description)
    }

    // ── getTotalBalanceByAccount ─────────────────────────────────────────

    @Test
    fun givenNoRecords_whenGetTotalBalanceByAccount_thenEmitsNull() = runTest {
        val result = dao.getTotalBalanceByAccount().first()
        assertNull(result)
    }

    @Test
    fun givenRecordsWithPositiveAndNegativeAmounts_whenGetTotalBalanceByAccount_thenEmitsSumOfAmounts() = runTest {
        dao.insert(
            buildRecord(amount = 200.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(150.0, result, 0.001)
    }

    @Test
    fun givenRecordsWithPositiveAmounts_whenGetTotalBalanceByAccount_thenEmitsSumOfAmounts() = runTest {
        dao.insert(
            buildRecord(amount = 200.0, accountId = 1),
            buildRecord(amount = 50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(250.0, result, 0.001)
    }

    @Test
    fun givenRecordsWithNegativeAmounts_whenGetTotalBalanceByAccount_thenEmitsSumOfAmounts() = runTest {
        dao.insert(
            buildRecord(amount = -200.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(-250.0, result, 0.001)
    }

    @Test
    fun givenRecordsWithAmountZero_whenGetTotalBalanceByAccount_thenEmitsSumOfAmounts() = runTest {
        dao.insert(
            buildRecord(amount = 0.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(-50.0, result, 0.001)
    }

    @Test
    fun givenRecordsWithDifferentAccounts_whenGetTotalBalanceByAccount_thenFiltersByAccount() = runTest {
        dao.insert(
            buildRecord(amount = 100.0, accountId = 1),
            buildRecord(amount = 300.0, accountId = 2)
        )

        val result = dao.getTotalBalanceByAccount(accId = 2).first()!!
        assertEquals(300.0, result, 0.001)
    }
}
