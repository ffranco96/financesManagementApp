package com.example.financesmanagementapp.ui.addregisteramount.domain

import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.SubcategoryName
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetRecordByIdUseCaseTest {

    private val mockRepository: RecordsRepository = mockk()
    private lateinit var getRecordByIdUseCase: GetRecordByIdUseCase

    @Before
    fun setUp() {
        getRecordByIdUseCase = GetRecordByIdUseCase(mockRepository)
    }

    @Test
    fun `given repository returns entity when invoked then maps it to a Record`() = runTest {
        val entity = buildEntity(id = 7)
        coEvery { mockRepository.getRecordById(7) } returns entity

        val result = getRecordByIdUseCase(7)

        assertNotNull(result)
        assertEquals(7, result!!.id)
        assertEquals(3, result.accountId)
        assertEquals(-123.45, result.amount, 0.001)
        assertEquals("Supermercado", result.description)
        assertEquals("2026-01-15T10:30:00", result.date)
        assertEquals("USD", result.currency)
        assertEquals(CategoryName.CATEGORY_FOOD_AND_DRINKS, result.category.categoryName)
        assertEquals(SubcategoryName.SUBCATEGORY_MARKET, result.category.subcategoryName)
        assertFalse(result.category.isIncome)
    }

    @Test
    fun `given repository returns income entity when invoked then maps the income category`() = runTest {
        val entity = buildEntity(
            id = 8,
            category = "CATEGORY_INCOME",
            subcategory = "SUBCATEGORY_SALARY",
        )
        coEvery { mockRepository.getRecordById(8) } returns entity

        val result = getRecordByIdUseCase(8)

        assertNotNull(result)
        assertEquals(CategoryName.CATEGORY_INCOME, result!!.category.categoryName)
        assertEquals(SubcategoryName.SUBCATEGORY_SALARY, result.category.subcategoryName)
        assertTrue(result.category.isIncome)
    }

    @Test
    fun `given repository returns null when invoked then returns null`() = runTest {
        coEvery { mockRepository.getRecordById(99) } returns null

        assertNull(getRecordByIdUseCase(99))
    }

    @Test
    fun `given repository returns entity when invoked then requests the given id`() = runTest {
        coEvery { mockRepository.getRecordById(42) } returns buildEntity(id = 42)

        getRecordByIdUseCase(42)

        coVerify(exactly = 1) { mockRepository.getRecordById(42) }
    }

    private fun buildEntity(
        id: Int,
        category: String = "CATEGORY_FOOD_AND_DRINKS",
        subcategory: String = "SUBCATEGORY_MARKET",
    ) = RecordEntity(
        id = id,
        accountId = 3,
        amount = -123.45,
        description = "Supermercado",
        category = category,
        subcategory = subcategory,
        date = "2026-01-15T10:30:00",
        currency = "USD",
    )
}
