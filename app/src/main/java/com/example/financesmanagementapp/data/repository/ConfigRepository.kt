package com.example.financesmanagementapp.data.repository

import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CryptoCurrency
import com.example.financesmanagementapp.domain.model.FiatCurrency
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun getFiatCurrencies(): Flow<List<FiatCurrency>>
    fun getCryptoCurrencies(): Flow<List<CryptoCurrency>>
    fun getCategories(): Flow<List<Category>>
    suspend fun saveFiatCurrencies(currencies: List<FiatCurrency>)
    suspend fun saveCryptoCurrencies(currencies: List<CryptoCurrency>)
    suspend fun saveCategories(categories: List<Category>)
}