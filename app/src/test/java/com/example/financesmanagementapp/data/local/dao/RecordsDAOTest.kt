package com.example.financesmanagementapp.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.financesmanagementapp.data.local.AppDatabase
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
    fun givenEmptyDatabase_whenGetAll_thenReturnEmptyList() = runTest {
        val result = dao.getAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun givenRecordsInserted_whenGetAll_thenReturnAllRecords() = runTest {
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
    fun givenPositiveAndNegativeAmounts_whenGetTotalBalanceByAccount_thenEmitsSum() = runTest {
        dao.insert(
            buildRecord(amount = 200.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(150.0, result, 0.001)
    }

    @Test
    fun givenPositiveAmounts_whenGetTotalBalanceByAccount_thenEmitsSum() = runTest {
        dao.insert(
            buildRecord(amount = 200.0, accountId = 1),
            buildRecord(amount = 50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(250.0, result, 0.001)
    }

    @Test
    fun givenNegativeAmounts_whenGetTotalBalanceByAccount_thenEmitsSum() = runTest {
        dao.insert(
            buildRecord(amount = -200.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(-250.0, result, 0.001)
    }

    @Test
    fun givenAmountZero_whenGetTotalBalanceByAccount_thenEmitsSum() = runTest {
        dao.insert(
            buildRecord(amount = 0.0, accountId = 1),
            buildRecord(amount = -50.0, accountId = 1)
        )

        val result = dao.getTotalBalanceByAccount(accId = 1).first()!!
        assertEquals(-50.0, result, 0.001)
    }

    @Test
    fun givenDifferentAccounts_whenGetTotalBalanceByAccount_thenFiltersByAccount() = runTest {
        dao.insert(
            buildRecord(amount = 100.0, accountId = 1),
            buildRecord(amount = 300.0, accountId = 2)
        )

        val result = dao.getTotalBalanceByAccount(accId = 2).first()!!
        assertEquals(300.0, result, 0.001)
    }

    // ── getBalanceByCategoryAndAccount ───────────────────────────────────

    @Test
    fun givenNoMatchingRecords_whenGetBalanceByCategoryAndAccount_thenEmitsNull() = runTest {
        val result = dao.getBalanceByCategoryAndAccount(categoryName = "Comida y alimentos").first()
        assertNull(result)
    }

    @Test
    fun givenMatchingCategory_whenGetBalanceByCategoryAndAccount_thenEmitsFilteredSum() = runTest {
        dao.insert(
            buildRecord(amount = 50.0, categoryName = "Comida y alimentos", accountId = 1),
            buildRecord(amount = 80.0, categoryName = "Transporte", accountId = 1),
            buildRecord(amount = 30.0, categoryName = "Comida y alimentos", accountId = 1)
        )

        val result = dao.getBalanceByCategoryAndAccount(accId = 1, categoryName = "Comida y alimentos").first()
        assertNotNull(result)
        assertEquals(80.0, result!!, 0.001)
    }

    @Test
    fun givenDifferentAccounts_whenGetBalanceByCategoryAndAccount_thenFiltersByAccount() = runTest {
        dao.insert(
            buildRecord(amount = 100.0, categoryName = "Comida y alimentos", accountId = 1),
            buildRecord(amount = 200.0, categoryName = "Comida y alimentos", accountId = 2)
        )

        val result = dao.getBalanceByCategoryAndAccount(accId = 2, categoryName = "Comida y alimentos").first()
        assertNotNull(result)
        assertEquals(200.0, result!!, 0.001)
    }

    // ── insert ───────────────────────────────────────────────────────────

    @Test
    fun givenRecord_whenInsert_thenRecordIsPersisted() = runTest {
        val record = buildRecord(description = "Registro insertado")
        dao.insert(record)

        val result = dao.getAll()
        assertEquals(1, result.size)
        assertEquals("Registro insertado", result[0].description)
    }

    @Test
    fun givenMultipleRecords_whenInsert_thenAllArePersisted() = runTest {
        val r1 = buildRecord(description = "Primero", amount = 10.0)
        val r2 = buildRecord(description = "Segundo", amount = 20.0)
        val r3 = buildRecord(description = "Tercero", amount = 30.0)
        dao.insert(r1, r2, r3)

        val result = dao.getAll()
        assertEquals(3, result.size)
    }

    // ── delete ───────────────────────────────────────────────────────────

    @Test
    fun givenExistingRecord_whenDelete_thenRecordIsRemoved() = runTest {
        val record = buildRecord(description = "Para borrar")
        dao.insert(record)
        val inserted = dao.getAll()[0]

        dao.delete(inserted)

        val result = dao.getAll()
        assertTrue(result.isEmpty())
    }

    // ── update ───────────────────────────────────────────────────────────

    @Test
    fun givenExistingRecord_whenUpdate_thenRecordReflectsChanges() = runTest {
        val record = buildRecord(description = "Original", amount = 10.0)
        dao.insert(record)
        val inserted = dao.getAll()[0]

        val updated = inserted.copy(description = "Actualizado", amount = 99.5)
        dao.update(updated)

        val result = dao.getAll()[0]
        assertEquals("Actualizado", result.description)
        assertEquals(99.5, result.amount, 0.001)
    }

    // ── deleteAll ────────────────────────────────────────────────────────

    @Test
    fun givenRecordsExist_whenDeleteAll_thenTableIsEmpty() = runTest {
        dao.insert(
            buildRecord(description = "A"),
            buildRecord(description = "B")
        )

        dao.deleteAll()

        val result = dao.getAll()
        assertTrue(result.isEmpty())
    }
}
