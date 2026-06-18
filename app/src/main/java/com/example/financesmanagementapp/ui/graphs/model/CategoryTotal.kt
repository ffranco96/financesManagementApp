package com.example.financesmanagementapp.ui.graphs.model

/**
 * Represents the net total (income - expense) for a single category,
 * used to render one bar in the charts screen.
 *
 * @property categoryName Display name of the category (e.g. "Comida y alimentos").
 * @property totalAmount Net amount: positive for net income, negative for net expense.
 * @property colorResId Resource ID of the color used to fill the bar.
 */
data class CategoryTotal(
    val categoryName: String,
    val totalAmount: Double,
    val colorResId: Int
)
