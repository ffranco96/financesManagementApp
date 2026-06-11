package com.example.financesmanagementapp.ui.graphs.ui

import com.example.financesmanagementapp.ui.graphs.model.CategorySpending

/**
 * UI state for the Charts screen.
 *
 * @property categorySpendings Aggregated spendings per category; empty when there are no records
 *   or all categories have a net of zero.
 * @property isEmpty Whether the chart has no data to display.
 */
data class ChartsUiState(
    val categorySpendings: List<CategorySpending> = emptyList(),
    val isEmpty: Boolean = true
)
