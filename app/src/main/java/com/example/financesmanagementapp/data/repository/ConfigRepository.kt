package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.domain.model.Currency
import com.example.financesmanagementapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun getFiatCurrencies(): Flow<List<Currency>>
    fun getCryptoCurrencies(): Flow<List<Currency>>
    fun getCategories(): Flow<List<Category>>
    suspend fun saveFiatCurrencies(currencies: List<Currency>)
    suspend fun saveCryptoCurrencies(currencies: List<Currency>)
    suspend fun saveCategories(categories: List<Category>)
}