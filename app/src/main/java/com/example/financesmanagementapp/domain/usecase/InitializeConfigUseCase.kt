package com.example.financesmanagementapp.domain.usecase

import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.example.financesmanagementapp.data.repository.ConfigRepository
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CryptoCurrency
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InitializeConfigUseCase @Inject constructor(
    private val repository: ConfigRepository
) {
    suspend operator fun invoke() {
        val fiat = repository.getFiatCurrencies().first()
        if (fiat.isEmpty()) {
            repository.saveFiatCurrencies(FiatCurrency.entries.toList())
        }

        val crypto = repository.getCryptoCurrencies().first()
        if (crypto.isEmpty()) {
            repository.saveCryptoCurrencies(CryptoCurrency.entries)
        }

        val categories = repository.getCategories().first()
        if (categories.isEmpty()) {
            repository.saveCategories(listOf(
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
