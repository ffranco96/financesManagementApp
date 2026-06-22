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
 * the last 30 days, groups them by category and computes separate totals for
 * income (positive amount) and expense (negative amount) per category.
 *
 * Categories can appear in both income and expense results when they have
 * records of both types. Zero-amount totals are excluded.
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
     * @return A [Flow] emitting the aggregated [CategoryTotal] list every time
     *   the underlying data changes (e.g. after a record insert or update).
     */
    operator fun invoke(accountId: Int): Flow<List<CategoryTotal>> {
        return repository.getAllRecordsFlow().map { entities ->
            val thirtyDaysAgo = LocalDate.now().minusDays(30)
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
                    recordDate != null && !recordDate.isBefore(thirtyDaysAgo)
                }
                .groupBy { it.categoryName }
                .flatMap { (categoryName, records) ->
                    val category = Category.fromName(categoryName)
                    val incomeTotal = records.filter { it.amount > 0 }.sumOf { it.amount }
                    val expenseTotal = records.filter { it.amount < 0 }.sumOf { it.amount }
                    listOfNotNull(
                        CategoryTotal(
                            categoryName = categoryName,
                            totalAmount = incomeTotal,
                            colorResId = category.colorIcon
                        ).takeIf { incomeTotal != 0.0 },
                        CategoryTotal(
                            categoryName = categoryName,
                            totalAmount = expenseTotal,
                            colorResId = category.colorIcon
                        ).takeIf { expenseTotal != 0.0 }
                    )
                }
        }
    }
}
