package com.example.financesmanagementapp.ui.graphs.domain

import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Record.Companion.DEFAULT_ACCOUNT_ID
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetCategoryTotalUseCaseTest {

    private val mockRepository: RecordsRepository = mockk()
    private lateinit var getCategoryTotalUseCase: GetCategoryTotalUseCase

    @Before
    fun setUp() {
        getCategoryTotalUseCase = GetCategoryTotalUseCase(mockRepository)
    }

    @Test
    fun `given records from different categories in last 30 days then returns grouped totals of incomes and expenses`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = -100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = -50.0, category = "CATEGORY_HEALTH", subcategory = "SUBCATEGORY_MEDICINE", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 200.0, category = "CATEGORY_INCOME", subcategory = "SUBCATEGORY_SALARY", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result: List<CategoryTotal> = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(3, result.size)
        val food = result.find { it.categoryName == "Supermercado y almacén" }!!
        assertEquals(0.0, food.incomes, 0.001)
        assertEquals(-100.0, food.expenses, 0.001)
        assertEquals(-100.0, food.net, 0.001)

        val health = result.find { it.categoryName == "Medicación" }!!
        assertEquals(0.0, health.incomes, 0.001)
        assertEquals(-50.0, health.expenses, 0.001)
        assertEquals(-50.0, health.net, 0.001)

        val salary = result.find { it.categoryName == "Sueldo" }!!
        assertEquals(200.0, salary.incomes, 0.001)
        assertEquals(0.0, salary.expenses, 0.001)
        assertEquals(200.0, salary.net, 0.001)
    }

    @Test
    fun `given records older than 30 days then filters them out`() = runTest {
        val today = LocalDate.now()
        val oldRecord = RecordEntity(amount = -100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.minusDays(45).toString(), currency = "ARS", description = "")
        val recentRecord = RecordEntity(amount = -50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.minusDays(10).toString(), currency = "ARS", description = "")
        every { mockRepository.getAllRecordsFlow() } returns flowOf(listOf(oldRecord, recentRecord))

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals(0.0, result[0].incomes, 0.001)
        assertEquals(-50.0, result[0].expenses, 0.001)
        assertEquals(-50.0, result[0].net, 0.001)
    }

    @Test
    fun `given record exactly 30 days ago then includes it`() = runTest {
        val today = LocalDate.now()
        val record = RecordEntity(amount = -100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.minusDays(30).toString(), currency = "ARS", description = "")
        every { mockRepository.getAllRecordsFlow() } returns flowOf(listOf(record))

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals(0.0, result[0].incomes, 0.001)
        assertEquals(-100.0, result[0].expenses, 0.001)
        assertEquals(-100.0, result[0].net, 0.001)
    }

    @Test
    fun `given no records then returns empty list`() = runTest {
        every { mockRepository.getAllRecordsFlow() } returns flowOf(emptyList())

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given multiple records in same category then sums them`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = -30.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = -50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals(0.0, result[0].incomes, 0.001)
        assertEquals(-80.0, result[0].expenses, 0.001)
        assertEquals(-80.0, result[0].net, 0.001)
    }

    @Test
    fun `given income and expense in same category then returns single entry with correct incomes, expenses and net`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = -100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals(50.0, result[0].incomes, 0.001)
        assertEquals(-100.0, result[0].expenses, 0.001)
        assertEquals(-50.0, result[0].net, 0.001)
    }

    @Test
    fun `given category with matching incomes and expenses then includes it`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = -100.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals(100.0, result[0].incomes, 0.001)
        assertEquals(-100.0, result[0].expenses, 0.001)
        assertEquals(0.0, result[0].net, 0.001)
    }

    @Test
    fun `given records from single category then returns single entry`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals("Supermercado y almacén", result[0].categoryName)
        assertEquals(50.0, result[0].incomes, 0.001)
        assertEquals(0.0, result[0].expenses, 0.001)
        assertEquals(50.0, result[0].net, 0.001)
    }

    @Test
    fun `given records for different accountId then filters by account`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "", accountId = 0),
            RecordEntity(amount = 100.0, category = "CATEGORY_HEALTH", subcategory = "SUBCATEGORY_MEDICINE", date = today.toString(), currency = "ARS", description = "", accountId = 1)
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(0, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(1, result.size)
        assertEquals("Supermercado y almacén", result[0].categoryName)
        assertEquals(50.0, result[0].incomes, 0.001)
    }

    @Test
    fun `maps category to CategoryTotal with correct colorResId`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, category = "CATEGORY_FOOD_AND_DRINKS", subcategory = "SUBCATEGORY_MARKET", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = getCategoryTotalUseCase(DEFAULT_ACCOUNT_ID, THIRTY_DAYS_TO_LOOK_BACKWARDS).first()

        assertEquals(R.color.categ_color_food_and_drinks_market, result[0].colorResId)
    }

    companion object {
        private const val THIRTY_DAYS_TO_LOOK_BACKWARDS = 30
    }
}
