package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.data.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to retrieve the total balance for a specific account as a Flow.
 *
 * @property repository The repository to fetch balance data from.
 */
class GetTotalBalanceByAccountUseCase @Inject constructor(
    private val repository: RecordsRepository
){
    operator fun invoke(accountId: Int): Flow<Double> {
        return repository.getTotalBalanceByAccountFlow(accountId)
            .map { it ?: 0.0 }
    }
}