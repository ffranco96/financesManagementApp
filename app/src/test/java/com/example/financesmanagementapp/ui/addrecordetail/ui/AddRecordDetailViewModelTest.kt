package com.example.financesmanagementapp.ui.addrecordetail.ui

import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.SubcategoryName
import com.example.financesmanagementapp.domain.usecase.GetCategoriesUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.UpdateRecordUseCase
import com.example.financesmanagementapp.util.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddRecordDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveRecordUseCase: SaveRecordUseCase = mockk(relaxed = true)
    private val updateRecordUseCase: UpdateRecordUseCase = mockk(relaxed = true)
    private val getCategoriesUseCase: GetCategoriesUseCase = mockk()

    private lateinit var viewModel: AddRecordDetailViewModel

    @Before
    fun setUp() {
        every { getCategoriesUseCase() } returns flowOf(emptyList())
        // The test dispatcher replaces the injected IO dispatcher so runTest drives the save coroutine.
        viewModel = AddRecordDetailViewModel(
            saveRecordUseCase = saveRecordUseCase,
            updateRecordUseCase = updateRecordUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `given record without category when save clicked then requires confirmation and does not save`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = newRecord(category = Category())

            val outcome = viewModel.onSaveClicked(record)

            advanceUntilIdle()

            assertEquals(SaveOutcome.CONFIRMATION_REQUIRED, outcome)
            assertTrue(viewModel.pendingCategoryConfirmation.value)
            coVerify(exactly = 0) { saveRecordUseCase(any()) }
            coVerify(exactly = 0) { updateRecordUseCase(any()) }
        }

    @Test
    fun `given new record with category when save clicked then saves immediately without confirmation`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = newRecord(category = selectedCategory())

            val outcome = viewModel.onSaveClicked(record)

            advanceUntilIdle()

            assertEquals(SaveOutcome.SAVED, outcome)
            assertFalse(viewModel.pendingCategoryConfirmation.value)
            coVerify(exactly = 1) { saveRecordUseCase(record) }
            coVerify(exactly = 0) { updateRecordUseCase(any()) }
        }

    @Test
    fun `given existing record with category when save clicked then updates instead of inserting`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = newRecord(category = selectedCategory(), id = 7)

            val outcome = viewModel.onSaveClicked(record)

            advanceUntilIdle()

            assertEquals(SaveOutcome.SAVED, outcome)
            coVerify(exactly = 1) { updateRecordUseCase(record) }
            coVerify(exactly = 0) { saveRecordUseCase(any()) }
        }

    @Test
    fun `given pending confirmation when confirmed then saves the pending record and clears the state`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = newRecord(category = Category())
            viewModel.onSaveClicked(record)

            viewModel.onSaveConfirmed()

            advanceUntilIdle()

            assertFalse(viewModel.pendingCategoryConfirmation.value)
            coVerify(exactly = 1) { saveRecordUseCase(record) }
        }

    @Test
    fun `given pending confirmation when dismissed then does not save and clears the state`() =
        runTest(mainDispatcherRule.testDispatcher) {
            viewModel.onSaveClicked(newRecord(category = Category()))

            viewModel.onSaveDismissed()

            advanceUntilIdle()

            assertFalse(viewModel.pendingCategoryConfirmation.value)
            coVerify(exactly = 0) { saveRecordUseCase(any()) }
            coVerify(exactly = 0) { updateRecordUseCase(any()) }
        }

    @Test
    fun `given no record when save clicked then ignores the request`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val outcome = viewModel.onSaveClicked(null)

            advanceUntilIdle()

            assertEquals(SaveOutcome.IGNORED, outcome)
            assertFalse(viewModel.pendingCategoryConfirmation.value)
            coVerify(exactly = 0) { saveRecordUseCase(any()) }
        }

    @Test
    fun `given pending confirmation when confirm clicked twice then saves only once`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val record = newRecord(category = Category())
            viewModel.onSaveClicked(record)

            // A fast double tap reaches the still composed dialog before recomposition hides it.
            viewModel.onSaveConfirmed()
            viewModel.onSaveConfirmed()

            advanceUntilIdle()

            coVerify(exactly = 1) { saveRecordUseCase(record) }
        }

    private fun newRecord(category: Category, id: Int = 0): Record = Record(
        id = id,
        amount = -1500.0,
        description = "Test record",
        category = category,
        date = "2026-01-15T10:30:00",
        currency = "ARS",
    )

    private fun selectedCategory(): Category = Category(
        categoryName = CategoryName.CATEGORY_SERVICES,
        subcategoryName = SubcategoryName.SUBCATEGORY_INTERNET,
        displayName = SubcategoryName.SUBCATEGORY_INTERNET.label,
    )
}
