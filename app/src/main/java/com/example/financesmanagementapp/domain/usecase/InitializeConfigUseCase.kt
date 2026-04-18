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
                Category("Comida y alimentos", R.drawable.ic_category_food, R.color.categ_color_food, ""),
                Category("Restaurant y comida rapida", R.drawable.ic_category_fast_food, R.color.categ_color_fast_food, ""),
                Category("Ropa", R.drawable.ic_category_clothes, R.color.categ_color_clothes, ""),
                Category("Vehiculos", R.drawable.ic_category_vehicle, R.color.categ_color_vehicles, ""),
                Category("Mantenimiento vehiculos", R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance, ""),
                Category("Recitales y eventos", R.drawable.ic_category_concerts, R.color.categ_color_concerts, ""),
                Category("Salud", R.drawable.ic_category_health, R.color.categ_color_health, ""),
                Category("Estudios particulares", R.drawable.ic_category_particular_studies, R.color.categ_color_studies, ""),
                Category("Medicamentos e insumos", R.drawable.ic_category_medicine, R.color.categ_color_medicine, ""),
                Category("Hobbies", R.drawable.ic_category_hobbies, R.color.categ_color_hobbies, ""),
                Category("Pintura, dibujo y fotografia", R.drawable.ic_category_painting_drawing_and_photography, R.color.categ_color_painting_drawing_photos, ""),
                Category("Inversiones y finanzas", R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances, ""),
                Category("Salario", R.drawable.ic_category_salary, R.color.categ_color_salary, ""),
                Category("Otros", R.drawable.ic_other_generic, R.color.categ_color_other, "")
            ))
        }
    }
}
