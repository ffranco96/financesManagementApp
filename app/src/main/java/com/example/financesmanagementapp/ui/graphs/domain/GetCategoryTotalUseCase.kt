package com.example.financesmanagementapp.ui.graphs.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Use case that retrieves all records for a given account, filters those within
 * the last 'daysToGetTotal' days, groups them by category and computes separate income,
 * expense and net totals per category.
 *
 * Each category appears exactly once in the result, with income and expense
 * subtotals plus their net (income + expense). Categories with both incomes
 * and expenses equal to zero are excluded.
 *
 * @property repository The [RecordsRepository] used to fetch the raw records.
 */
class GetCategoryTotalUseCase @Inject constructor(
    private val repository: RecordsRepository
) {
    /**
     * Executes the use case.
     *
     * @param accountId The ID of the account to filter records by.
     * @daysToGetTotal: Quantity of days from the current day to the past to obtain the total.
     * @return A [Flow] emitting the aggregated [CategoryTotal] list every time
     *   the underlying data changes (e.g. after a record insert or update).
     */
    operator fun invoke(accountId: Int, daysToGetTotal: Int): Flow<List<CategoryTotal>> {
        return repository.getAllRecordsFlow().map { entities ->
            val nDaysAgo = LocalDate.now().minusDays(daysToGetTotal.toLong())
            entities
                .filter { it.accountId == accountId }
                .filter { entity ->
                    val recordDate = try {
                        LocalDateTime.parse(entity.date).toLocalDate()
                    } catch (_: Exception) {
                        try {
                            LocalDate.parse(entity.date)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    recordDate != null && !recordDate.isBefore(nDaysAgo)
                }
                .groupBy { it.categoryName }
                .map { (categoryName, records) ->
                    val category = Category.fromName(categoryName)
                    val incomes = records.filter { it.amount > 0 }.sumOf { it.amount }
                    val expenses = records.filter { it.amount < 0 }.sumOf { it.amount }
                    CategoryTotal(
                        categoryName = categoryName,
                        incomes = incomes,
                        expenses = expenses,
                        net = incomes + expenses,
                        colorResId = category.colorIcon
                    )
                }
                .filter { it.incomes != 0.0 || it.expenses != 0.0 }
        }
    }
}
