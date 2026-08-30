package com.example.financesmanagementapp.ui.graphs.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to retrieve the balance for a specific account and category as a Flow.
 *
 * @property repository The repository to fetch balance data from.
 */
class GetBalanceByAccountAndCategoryUseCase @Inject constructor(
    private val repository: RecordsRepository
) {
    /**
     * Executes the use case to get the balance flow.
     * Maps null results to 0.0 to ensure a non-null Double stream.
     *
     * @param accountId The ID of the account.
     * @param category The parent category constant to filter by.
     * @param subcategory The subcategory constant to filter by.
     * @return A Flow emitting the calculated balance.
     */
    operator fun invoke(accountId: Int, category: String, subcategory: String): Flow<Double> {
        return repository.getBalanceByAccountAndCategoryFlow(accountId, category, subcategory)
            .map { it ?: 0.0 }
    }
}
