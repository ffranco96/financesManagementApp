package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.R

/**
 * Data class representing the category of a movement in the app.
 * The categories can be: Food, Health, Hobbies, Investments, etc.
 */
data class Category(
    var categoryName: String = CATEGORY_MISSING,
    var iconRsc: Int = R.drawable.ic_other_generic,
    var colorIcon: Int = R.color.sad_grey,
    var details: String = ""
) {
    companion object {
        const val CATEGORY_FOOD = "Comida y alimentos"
        const val CATEGORY_FAST_FOOD = "Restaurant y comida rapida"
        const val CATEGORY_CLOTHES = "Ropa"
        const val CATEGORY_VEHICLES = "Vehiculos"
        const val CATEGORY_VEHICLE_MAINTENANCE = "Mantenimiento vehiculos"
        const val CATEGORY_CONCERTS = "Recitales y eventos"
        const val CATEGORY_HEALTH = "Salud"
        const val CATEGORY_STUDIES = "Estudios particulares"
        const val CATEGORY_MEDICINE = "Medicamentos e insumos"
        const val CATEGORY_HOBBIES = "Hobbies"
        const val CATEGORY_ART_PHOTO = "Pintura, dibujo y fotografia"
        const val CATEGORY_INVESTMENT = "Inversiones y finanzas"
        const val CATEGORY_SALARY = "Salario"
        const val CATEGORY_MISSING = "Faltantes"
    }
}
