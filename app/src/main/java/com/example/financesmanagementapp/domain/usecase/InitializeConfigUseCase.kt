package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.R
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
        if (categories.isEmpty()) {
            configRepository.saveCategories(listOf(
                Category(Category.CATEGORY_FOOD, R.drawable.ic_category_food, R.color.categ_color_food, ""),
                Category(Category.CATEGORY_FAST_FOOD, R.drawable.ic_category_fast_food, R.color.categ_color_fast_food, ""),
                Category(Category.CATEGORY_CLOTHES, R.drawable.ic_category_clothes, R.color.categ_color_clothes, ""),
                Category(Category.CATEGORY_VEHICLES, R.drawable.ic_category_vehicle, R.color.categ_color_vehicles, ""),
                Category(Category.CATEGORY_VEHICLE_MAINTENANCE, R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance, ""),
                Category(Category.CATEGORY_CONCERTS, R.drawable.ic_category_concerts, R.color.categ_color_concerts, ""),
                Category(Category.CATEGORY_HEALTH, R.drawable.ic_category_health, R.color.categ_color_health, ""),
                Category(Category.CATEGORY_STUDIES, R.drawable.ic_category_particular_studies, R.color.categ_color_studies, ""),
                Category(Category.CATEGORY_MEDICINE, R.drawable.ic_category_medicine, R.color.categ_color_medicine, ""),
                Category(Category.CATEGORY_HOBBIES, R.drawable.ic_category_hobbies, R.color.categ_color_hobbies, ""),
                Category(Category.CATEGORY_ART_PHOTO, R.drawable.ic_category_painting_drawing_and_photography, R.color.categ_color_painting_drawing_photos, ""),
                Category(Category.CATEGORY_INVESTMENT, R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances, ""),
                Category(Category.CATEGORY_SALARY, R.drawable.ic_category_salary, R.color.categ_color_salary, ""),
                Category(Category.CATEGORY_MISSING, R.drawable.ic_other_generic, R.color.categ_color_other, "")
            ))
        }
    }
}
