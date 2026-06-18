package com.example.financesmanagementapp.ui.graphs.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case that retrieves all records for a given account, filters those within
 * the last 30 days, groups them by category and computes the net amount
 * (income - expense) per category.
 *
 * Categories with a net amount of exactly 0.0 are excluded from the result.
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
                    try {
                        val date = LocalDate.parse(entity.date)
                        !date.isBefore(thirtyDaysAgo)
                    } catch (_: Exception) {
                        false
                    }
                }
                .groupBy { it.categoryName }
                .map { (categoryName, records) ->
                    val total = records.sumOf { record ->
                        record.amount
                    }
                    val category = Category.fromName(categoryName)
                    CategoryTotal(
                        categoryName = categoryName,
                        totalAmount = total,
                        colorResId = category.colorIcon
                    )
                }
                .filter { it.totalAmount != 0.0 }
        }
    }
}
