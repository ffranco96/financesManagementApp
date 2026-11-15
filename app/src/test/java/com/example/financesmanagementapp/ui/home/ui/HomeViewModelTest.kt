package com.example.financesmanagementapp.ui.home.ui

import android.net.Uri
import com.example.financesmanagementapp.data.local.ExportCsvUseCase
import com.example.financesmanagementapp.data.local.ParseCsvUseCase
import com.example.financesmanagementapp.data.local.ReadCsvUseCase
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.SubcategoryName
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.home.domain.DeleteAllRecordsUseCase
import com.example.financesmanagementapp.ui.home.domain.DeleteRecordUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllCryptoPricesUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllRecordsFlowUseCase
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceByTickerUseCase
import com.example.financesmanagementapp.ui.home.domain.GetTotalBalanceByAccountUseCase
import com.example.financesmanagementapp.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests the CSV export contract of [HomeViewModel]: the two-phase flow where
 * the ViewModel first asks the UI to open the system file picker, and then
 * writes to the document the user picked.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getBtcPriceUseCase: GetCryptoPriceByTickerUseCase = mockk(relaxed = true)
    private val getAllCryptoPricesUseCase: GetAllCryptoPricesUseCase = mockk(relaxed = true)
    private val getAllRecordsFlowUseCase: GetAllRecordsFlowUseCase = mockk()
    private val deleteAllRecordsUseCase: DeleteAllRecordsUseCase = mockk(relaxed = true)
    private val deleteRecordUseCase: DeleteRecordUseCase = mockk(relaxed = true)
    private val readCsvUseCase: ReadCsvUseCase = mockk(relaxed = true)
    private val parseCsvUseCase: ParseCsvUseCase = mockk(relaxed = true)
    private val saveRecordUseCase: SaveRecordUseCase = mockk(relaxed = true)
    private val exportCsvUseCase: ExportCsvUseCase = mockk()
    private val getTotalBalanceUseCase: GetTotalBalanceByAccountUseCase = mockk()

    private val recordsFlow = MutableStateFlow<List<Record>>(emptyList())
    private val destination: Uri = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        every { getAllRecordsFlowUseCase() } returns recordsFlow
        every { getTotalBalanceUseCase(any()) } returns flowOf(0.0)
        every { exportCsvUseCase.suggestedFileName() } returns SUGGESTED_FILE_NAME

        viewModel = HomeViewModel(
            getBtcPriceUseCase = getBtcPriceUseCase,
            getAllCryptoPricesUseCase = getAllCryptoPricesUseCase,
            getAllRecordsFlowUseCase = getAllRecordsFlowUseCase,
            deleteAllRecordsUseCase = deleteAllRecordsUseCase,
            deleteRecordUseCase = deleteRecordUseCase,
            readCsvUseCase = readCsvUseCase,
            parseCsvUseCase = parseCsvUseCase,
            saveRecordUseCase = saveRecordUseCase,
            exportCsvUseCase = exportCsvUseCase,
            getTotalBalanceUseCase = getTotalBalanceUseCase,
        )
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    /** Keeps [HomeViewModel.recordsList] hot so its value reflects the repository flow. */
    private fun TestScope.startCollectingRecords() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.recordsList.collect { }
        }
    }

    /** Captures every [HomeUiEvent] the ViewModel emits during the test. */
    private fun TestScope.startCollectingEvents(events: MutableList<HomeUiEvent>) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.exportedCsvEvent.collect { events.add(it) }
        }
    }

    private fun newRecord() = Record(
        amount = -30000.0,
        description = "Farmacia",
        category = Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE),
        date = "2026-04-09",
        currency = "ARS"
    )

    // ── Tests ────────────────────────────────────────────────────────────

    @Test
    fun `given records when requestCsvExport then emits launch event with suggested file name`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startCollectingRecords()
            recordsFlow.value = listOf(newRecord())
            advanceUntilIdle()

            val events = mutableListOf<HomeUiEvent>()
            startCollectingEvents(events)

            viewModel.requestCsvExport()
            advanceUntilIdle()

            assertEquals(listOf(HomeUiEvent.LaunchCsvExport(SUGGESTED_FILE_NAME)), events)
        }

    @Test
    fun `given no records when requestCsvExport then emits toast and does not open picker`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startCollectingRecords()
            advanceUntilIdle()

            val events = mutableListOf<HomeUiEvent>()
            startCollectingEvents(events)

            viewModel.requestCsvExport()
            advanceUntilIdle()

            assertEquals(listOf(HomeUiEvent.ShowToast("No hay registros para exportar")), events)
        }

    @Test
    fun `given records when exportCsv then writes to chosen uri and emits success toast`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startCollectingRecords()
            val records = listOf(newRecord())
            recordsFlow.value = records
            advanceUntilIdle()
            coEvery { exportCsvUseCase(records, destination) } returns true

            val events = mutableListOf<HomeUiEvent>()
            startCollectingEvents(events)

            viewModel.exportCsv(destination)
            advanceUntilIdle()

            coVerify(exactly = 1) { exportCsvUseCase(records, destination) }
            assertEquals(listOf(HomeUiEvent.ShowToast("CSV exportado exitosamente")), events)
        }

    @Test
    fun `given export fails when exportCsv then emits error toast`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startCollectingRecords()
            recordsFlow.value = listOf(newRecord())
            advanceUntilIdle()
            coEvery { exportCsvUseCase(any(), destination) } returns false

            val events = mutableListOf<HomeUiEvent>()
            startCollectingEvents(events)

            viewModel.exportCsv(destination)
            advanceUntilIdle()

            assertEquals(listOf(HomeUiEvent.ShowToast("Error al exportar CSV")), events)
        }

    @Test
    fun `given no records when exportCsv then emits toast and does not write`() =
        runTest(mainDispatcherRule.testDispatcher) {
            startCollectingRecords()
            advanceUntilIdle()

            val events = mutableListOf<HomeUiEvent>()
            startCollectingEvents(events)

            viewModel.exportCsv(destination)
            advanceUntilIdle()

            coVerify(exactly = 0) { exportCsvUseCase(any(), any()) }
            assertEquals(listOf(HomeUiEvent.ShowToast("No hay registros para exportar")), events)
        }

    private companion object {
        const val SUGGESTED_FILE_NAME = "records_20260101_120000.csv"
    }
}
