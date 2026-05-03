package com.example.financesmanagementapp.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CryptoCurrency
import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config_prefs")

@Singleton
class ConfigRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ConfigRepository {

    private object PreferencesKeys {
        val FIAT_CURRENCIES = stringSetPreferencesKey("fiat_currencies")
        val CRYPTO_CURRENCIES = stringSetPreferencesKey("crypto_currencies")
        val CATEGORIES = stringSetPreferencesKey("categories")
    }

    override fun getFiatCurrencies(): Flow<List<FiatCurrency>> = context.dataStore.data.map { preferences ->
        val set = preferences[PreferencesKeys.FIAT_CURRENCIES] ?: return@map emptyList()
        try {
            set.map { gson.fromJson(it, FiatCurrency::class.java) }
        } catch (e: Exception) {
            Log.e("ConfigRepository", "Error parsing FiatCurrencies, returning empty list to trigger re-initialization", e)
            emptyList()
        }
    }

    override fun getCryptoCurrencies(): Flow<List<CryptoCurrency>> = context.dataStore.data.map { preferences ->
        val set = preferences[PreferencesKeys.CRYPTO_CURRENCIES] ?: return@map emptyList()
        try {
            set.map { gson.fromJson(it, CryptoCurrency::class.java) }
        } catch (e: Exception) {
            Log.e("ConfigRepository", "Error parsing CryptoCurrencies, returning empty list to trigger re-initialization", e)
            emptyList()
        }
    }

    override fun getCategories(): Flow<List<Category>> = context.dataStore.data.map { preferences ->
        val set = preferences[PreferencesKeys.CATEGORIES] ?: return@map emptyList()
        try {
            set.map { gson.fromJson(it, Category::class.java) }
        } catch (e: Exception) {
            Log.e("ConfigRepository", "Error parsing Categories, returning empty list to trigger re-initialization", e)
            emptyList()
        }
    }

    override suspend fun saveFiatCurrencies(currencies: List<FiatCurrency>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIAT_CURRENCIES] = currencies.map { gson.toJson(it) }.toSet()
        }
    }

    override suspend fun saveCryptoCurrencies(currencies: List<CryptoCurrency>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CRYPTO_CURRENCIES] = currencies.map { gson.toJson(it) }.toSet()
        }
    }

    override suspend fun saveCategories(categories: List<Category>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CATEGORIES] = categories.map { gson.toJson(it) }.toSet()
        }
    }
}
