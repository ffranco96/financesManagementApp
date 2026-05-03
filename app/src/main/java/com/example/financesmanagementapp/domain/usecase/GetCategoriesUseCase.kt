package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}
