package com.example.financesmanagementapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.financesmanagementapp.domain.model.Currency
import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
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

    override fun getFiatCurrencies(): Flow<List<Currency>> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FIAT_CURRENCIES]?.map { gson.fromJson(it, Currency::class.java) } ?: emptyList()
    }

    override fun getCryptoCurrencies(): Flow<List<Currency>> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.CRYPTO_CURRENCIES]?.map { gson.fromJson(it, Currency::class.java) } ?: emptyList()
    }

    override fun getCategories(): Flow<List<Category>> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.CATEGORIES]?.map { gson.fromJson(it, Category::class.java) } ?: emptyList()
    }

    override suspend fun saveFiatCurrencies(currencies: List<Currency>) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIAT_CURRENCIES] = currencies.map { gson.toJson(it) }.toSet()
        }
    }

    override suspend fun saveCryptoCurrencies(currencies: List<Currency>) {
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
