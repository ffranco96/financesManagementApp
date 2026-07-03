package com.example.financesmanagementapp.ui.graphs.model

/**
 * Represents the totals for a single category, including separate income
 * and expense subtotals and their net (income - expense).
 *
 * @property categoryName Display name of the category (e.g. "Comida y alimentos").
 * @property incomes Sum of all income amounts for this category (always >= 0).
 * @property expenses Sum of all expense amounts for this category as absolute value (always >= 0).
 * @property net Net amount: incomes - expenses (can be positive, negative or zero).
 * @property colorResId Resource ID of the color used to fill the bar.
 */
data class CategoryTotal(
    val categoryName: String,
    val incomes: Double,
    val expenses: Double,
    val net: Double,
    val colorResId: Int
)
