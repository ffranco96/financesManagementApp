package com.example.financesmanagementapp.ui.graphs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.ui.graphs.domain.GetCategoryTotalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Charts screen. Collects totals from
 * [GetCategoryTotalUseCase] and exposes the aggregated data as [ChartsUiState]
 * to drive the bar chart composable.
 *
 * @property getCategoryTotalUseCase Use case that provides per-category net amounts.
 */
@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getCategoryTotalUseCase: GetCategoryTotalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCategoryTotalUseCase(Record.DEFAULT_ACCOUNT_ID).collectLatest { total ->
                _uiState.value = ChartsUiState(
                    categoryTotals = total,
                    isEmpty = total.isEmpty()
                )
            }
        }
    }
}
