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
    private lateinit var useCase: GetCategoryTotalUseCase

    @Before
    fun setUp() {
        useCase = GetCategoryTotalUseCase(mockRepository)
    }

    @Test
    fun `given records from different categories in last 30 days then returns grouped totals`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 100.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 50.0, isIncome = false, categoryName = "Salud", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 200.0, isIncome = true, categoryName = "Salario", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result: List<CategoryTotal> = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(3, result.size)
        assertEquals(-100.0, result.find { it.categoryName == "Comida y alimentos" }!!.totalAmount, 0.001)
        assertEquals(-50.0, result.find { it.categoryName == "Salud" }!!.totalAmount, 0.001)
        assertEquals(200.0, result.find { it.categoryName == "Salario" }!!.totalAmount, 0.001)
    }

    @Test
    fun `given records older than 30 days then filters them out`() = runTest {
        val today = LocalDate.now()
        val oldRecord = RecordEntity(amount = 100.0, isIncome = false, categoryName = "Comida y alimentos", date = today.minusDays(45).toString(), currency = "ARS", description = "")
        val recentRecord = RecordEntity(amount = 50.0, isIncome = false, categoryName = "Comida y alimentos", date = today.minusDays(10).toString(), currency = "ARS", description = "")
        every { mockRepository.getAllRecordsFlow() } returns flowOf(listOf(oldRecord, recentRecord))

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(1, result.size)
        assertEquals(-50.0, result[0].totalAmount, 0.001)
    }

    @Test
    fun `given record exactly 30 days ago then includes it`() = runTest {
        val today = LocalDate.now()
        val record = RecordEntity(amount = 100.0, isIncome = false, categoryName = "Comida y alimentos", date = today.minusDays(30).toString(), currency = "ARS", description = "")
        every { mockRepository.getAllRecordsFlow() } returns flowOf(listOf(record))

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(1, result.size)
        assertEquals(-100.0, result[0].totalAmount, 0.001)
    }

    @Test
    fun `given no records then returns empty list`() = runTest {
        every { mockRepository.getAllRecordsFlow() } returns flowOf(emptyList())

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given multiple records in same category then sums them`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 30.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 50.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(1, result.size)
        assertEquals(-80.0, result[0].totalAmount, 0.001)
    }

    @Test
    fun `given income and expense in same category then calculates net`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 100.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 50.0, isIncome = true, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(1, result.size)
        assertEquals(-50.0, result[0].totalAmount, 0.001)
    }

    @Test
    fun `given category with net zero then excludes it`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 100.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = ""),
            RecordEntity(amount = 100.0, isIncome = true, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given records from single category then returns single total`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(1, result.size)
        assertEquals("Comida y alimentos", result[0].categoryName)
    }

    @Test
    fun `given records for different accountId then filters by account`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "", accountId = 0),
            RecordEntity(amount = 100.0, isIncome = false, categoryName = "Salud", date = today.toString(), currency = "ARS", description = "", accountId = 1)
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(0).first()

        assertEquals(1, result.size)
        assertEquals("Comida y alimentos", result[0].categoryName)
    }

    @Test
    fun `maps category to CategoryTotal with correct colorResId`() = runTest {
        val today = LocalDate.now()
        val records = listOf(
            RecordEntity(amount = 50.0, isIncome = false, categoryName = "Comida y alimentos", date = today.toString(), currency = "ARS", description = "")
        )
        every { mockRepository.getAllRecordsFlow() } returns flowOf(records)

        val result = useCase(DEFAULT_ACCOUNT_ID).first()

        assertEquals(R.color.categ_color_food, result[0].colorResId)
    }
}
