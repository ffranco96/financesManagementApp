package com.example.financesmanagementapp.ui.graphs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.crash.CrashReporter
import com.example.financesmanagementapp.ui.graphs.domain.GetCategoryTotalUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllRecordsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Charts screen. Collects totals from
 * [GetCategoryTotalUseCase] and exposes the aggregated data as [ChartsUiState]
 * to drive the bar chart composable.
 *
 * @property getCategoryTotalUseCase Use case that provides per-category net amounts.
 * @property getAllRecordsFlowUseCase Use case that provides all records as domain [Record].
 * @property crashReporter Reports non-fatals from the totals flow instead of silently falling
 * back to an empty state.
 */
@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getCategoryTotalUseCase: GetCategoryTotalUseCase,
    private val getAllRecordsFlowUseCase: GetAllRecordsFlowUseCase,
    private val crashReporter: CrashReporter
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    private val _allRecords = MutableStateFlow<List<Record>>(emptyList())
    val allRecords: StateFlow<List<Record>> = _allRecords.asStateFlow()

    init {
        viewModelScope.launch {
            combine( // It doesn't restart the values, keeps last value
                getCategoryTotalUseCase(Record.DEFAULT_ACCOUNT_ID, DAYS_TO_LOOK_BACK)
                    .retry(2)
                    .catch {
                        crashReporter.recordException(it, "Error loading category totals for Charts")
                        _uiState.value = ChartsUiState(isEmpty = true)
                    },
                getAllRecordsFlowUseCase()
                    .retry(2)
                    .catch {
                        crashReporter.recordException(it, "Error loading records for Charts")
                        _uiState.value = ChartsUiState(isEmpty = true)
                    }
            ) { totals, records ->
                ChartsUiState(categoryTotals = totals, isEmpty = totals.isEmpty()) to records
            }.collect { (uiState, records) ->
                _uiState.value = uiState
                _allRecords.value = records
            }
        }
    }

    companion object {
        private const val DAYS_TO_LOOK_BACK = 30
    }
}
