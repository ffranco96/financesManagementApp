package com.example.financesmanagementapp.ui.graphs.ui

import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.graphs.domain.GetCategoryTotalUseCase
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import com.example.financesmanagementapp.ui.home.domain.GetAllRecordsFlowUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChartsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockUseCase: GetCategoryTotalUseCase = mockk()
    private val mockAllRecordsUseCase: GetAllRecordsFlowUseCase = mockk()
    private val totalsFlow = MutableStateFlow<List<CategoryTotal>>(emptyList())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockUseCase.invoke(any(), any()) } returns totalsFlow
        every { mockAllRecordsUseCase.invoke() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given use case returns totals then uiState has data and isEmpty is false`() = runTest(testDispatcher) {
        val totals = listOf(
            CategoryTotal("Comida y alimentos", -150.0, R.color.categ_color_food),
            CategoryTotal("Salud", -50.0, R.color.categ_color_health)
        )
        totalsFlow.value = totals

        val viewModel = ChartsViewModel(mockUseCase, mockAllRecordsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertEquals(2, state.categoryTotals.size)
        assertFalse(state.isEmpty)
    }

    @Test
    fun `given use case returns empty list then uiState isEmpty is true`() = runTest(testDispatcher) {
        totalsFlow.value = emptyList()

        val viewModel = ChartsViewModel(mockUseCase, mockAllRecordsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        assertTrue(state.categoryTotals.isEmpty())
        assertTrue(state.isEmpty)
    }

    @Test
    fun `given new emission from use case then uiState updates reactively`() = runTest(testDispatcher) {
        totalsFlow.value = emptyList()
        val viewModel = ChartsViewModel(mockUseCase, mockAllRecordsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.uiState.value.categoryTotals.size)

        val newTotals = listOf(
            CategoryTotal("Comida y alimentos", -80.0, R.color.categ_color_food)
        )
        totalsFlow.value = newTotals
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.categoryTotals.size)
        assertEquals(-80.0, viewModel.uiState.value.categoryTotals[0].totalAmount, 0.001)
        assertFalse(viewModel.uiState.value.isEmpty)
    }
}
