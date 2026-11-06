package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.SavedStateHandle
import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.usecase.GetFiatCurrenciesUseCase
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.addregisteramount.domain.GetRecordByIdUseCase
import com.example.financesmanagementapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecordAmountViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase = mockk()
    private val getRecordByIdUseCase: GetRecordByIdUseCase = mockk()

    @Before
    fun setUp() {
        every { getFiatCurrenciesUseCase() } returns flowOf(
            listOf(FiatCurrency.ARS, FiatCurrency.USD)
        )
    }

    @Test
    fun `given recordId -1 when initialized then isEditing is false and repository is not queried`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = createViewModel(AppScreens.AddRecordAmountScreen.NO_RECORD_ID)

            advanceUntilIdle() // To make sure that the asynchronous loading of the viewmodel is finished.

            assertFalse(viewModel.isEditing)
            assertNull(viewModel.editingRecord.value)
            coVerify(exactly = 0) { getRecordByIdUseCase(any()) }
        }

    @Test
    fun `given existing record when initialized then preloads absolute amount and currency`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = Record(id = 7, amount = -123.45, currency = "USD")
            coEvery { getRecordByIdUseCase(7) } returns record
            val viewModel = createViewModel(7)

            backgroundScope.launch { viewModel.amountText.collect { } }
            advanceUntilIdle()

            assertTrue(viewModel.isEditing)
            assertEquals(record, viewModel.editingRecord.value)
            assertEquals("123.45", viewModel.amountText.value)
            assertEquals("USD", viewModel.selectedCurrency.value)
        }

    @Test
    fun `given record with blank currency when initialized then defaults to ARS`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getRecordByIdUseCase(7) } returns Record(
                id = 7,
                amount = 50.0,
                currency = "",
            )
            val viewModel = createViewModel(7)

            advanceUntilIdle()

            assertEquals(FiatCurrency.ARS.code, viewModel.selectedCurrency.value)
        }

    @Test
    fun `given recordId with no matching record when initialized then editingRecord is null and isEditing is true`() =
        runTest(mainDispatcherRule.testDispatcher) {
            coEvery { getRecordByIdUseCase(99) } returns null
            val viewModel = createViewModel(99)

            advanceUntilIdle()

            assertTrue(viewModel.isEditing)
            assertNull(viewModel.editingRecord.value)
        }

    @Test
    fun `given digits when onAmountTextChange then formats them as cents`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = createViewModel(AppScreens.AddRecordAmountScreen.NO_RECORD_ID)
            backgroundScope.launch { viewModel.amountText.collect { } }
            advanceUntilIdle()

            viewModel.onAmountTextChange("123")
            advanceUntilIdle()

            assertEquals("1.23", viewModel.amountText.value)
        }

    @Test
    fun `given more than max digits when onAmountTextChange then ignores the change`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val viewModel = createViewModel(AppScreens.AddRecordAmountScreen.NO_RECORD_ID)
            backgroundScope.launch { viewModel.amountText.collect { } }
            advanceUntilIdle()
            val tooLong = "1".repeat(AddRecordAmountViewModel.MAX_AMOUNT_TEXT_LENGTH + 1)

            viewModel.onAmountTextChange(tooLong)
            advanceUntilIdle()

            assertEquals("0.00", viewModel.amountText.value)
        }

    private fun createViewModel(recordId: Int): AddRecordAmountViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(AppScreens.AddRecordAmountScreen.RECORD_ID_ARG to recordId)
        )
        return AddRecordAmountViewModel(
            getFiatCurrenciesUseCase = getFiatCurrenciesUseCase,
            getRecordByIdUseCase = getRecordByIdUseCase,
            savedStateHandle = savedStateHandle,
        )
    }
}
