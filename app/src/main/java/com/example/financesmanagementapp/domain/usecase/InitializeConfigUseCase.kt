package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CryptoCurrency
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for initializing the configuration of the app.
 * This includes saving default fiat currencies, crypto currencies, and categories.
 * If the data already exists, it will not be overwritten.
 *
 * @param configRepository The repository for configuration data.
 * @constructor Creates an instance of InitializeConfigUseCase.
 * //IMPROVEMENT: Agregar categorias desde un archivo de configuracion
 */
class InitializeConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    /**
     * Initializes the configuration of the app by obtaining fiat and crypto currencies from
     * the config repository.
     *
     * If the data does not exist, it will be saved with default values.
     */
    suspend operator fun invoke() {
        val fiat = configRepository.getFiatCurrencies().first()
        if (fiat.isEmpty()) {
            configRepository.saveFiatCurrencies(FiatCurrency.entries.toList())
        }

        val crypto = configRepository.getCryptoCurrencies().first()
        if (crypto.isEmpty()) {
            configRepository.saveCryptoCurrencies(CryptoCurrency.entries)
        }

        val categories = configRepository.getCategories().first()
        val categoriesVersion = configRepository.getCategoriesVersion().first()
        if (categories.isEmpty() || categoriesVersion < Category.CATEGORIES_VERSION) {
            configRepository.saveCategories(Category.ALL_CATEGORIES)
            configRepository.saveCategoriesVersion(Category.CATEGORIES_VERSION)
        }
    }
}
