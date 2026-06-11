package com.example.financesmanagementapp.ui.graphs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.ui.graphs.domain.GetCategorySpendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Charts screen. Collects spendings from
 * [GetCategorySpendingUseCase] and exposes the aggregated data as [ChartsUiState]
 * to drive the bar chart composable.
 *
 * @property getCategorySpendingUseCase Use case that provides per-category net amounts.
 */
@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getCategorySpendingUseCase: GetCategorySpendingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCategorySpendingUseCase(Record.DEFAULT_ACCOUNT_ID).collectLatest { spendings ->
                _uiState.value = ChartsUiState(
                    categorySpendings = spendings,
                    isEmpty = spendings.isEmpty()
                )
            }
        }
    }
}
