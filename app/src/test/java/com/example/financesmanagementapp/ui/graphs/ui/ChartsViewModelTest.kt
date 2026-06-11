package com.example.financesmanagementapp.ui.graphs.ui

import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.graphs.domain.GetCategorySpendingUseCase
import com.example.financesmanagementapp.ui.graphs.model.CategorySpending
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChartsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockUseCase: GetCategorySpendingUseCase = mockk()
    private val spendingsFlow = MutableStateFlow<List<CategorySpending>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockUseCase.invoke(any()) } returns spendingsFlow
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given use case returns spendings then uiState has data and isEmpty is false`() = runTest(testDispatcher) {
        val spendings = listOf(
            CategorySpending("Comida y alimentos", -150.0, R.color.categ_color_food),
            CategorySpending("Salud", -50.0, R.color.categ_color_health)
        )
        spendingsFlow.value = spendings

        val viewModel = ChartsViewModel(mockUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(2, state.categorySpendings.size)
        assertFalse(state.isEmpty)
    }

    @Test
    fun `given use case returns empty list then uiState isEmpty is true`() = runTest(testDispatcher) {
        spendingsFlow.value = emptyList()

        val viewModel = ChartsViewModel(mockUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertTrue(state.categorySpendings.isEmpty())
        assertTrue(state.isEmpty)
    }

    @Test
    fun `given new emission from use case then uiState updates reactively`() = runTest(testDispatcher) {
        spendingsFlow.value = emptyList()
        val viewModel = ChartsViewModel(mockUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.categorySpendings.size)

        val newSpendings = listOf(
            CategorySpending("Comida y alimentos", -80.0, R.color.categ_color_food)
        )
        spendingsFlow.value = newSpendings
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.categorySpendings.size)
        assertEquals(-80.0, viewModel.uiState.value.categorySpendings[0].totalAmount, 0.001)
        assertFalse(viewModel.uiState.value.isEmpty)
    }
}
