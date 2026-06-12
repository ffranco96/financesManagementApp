package com.example.financesmanagementapp.ui.graphs.ui

import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal

/**
 * UI state for the Charts screen.
 *
 * @property categoryTotals Aggregated totals per category; empty when there are no records
 *   or all categories have a net of zero.
 * @property isEmpty Whether the chart has no data to display.
 */
data class ChartsUiState(
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val isEmpty: Boolean = true
)
