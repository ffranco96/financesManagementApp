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

        fun fromName(name: String): Category {
            return when (name) {
                CATEGORY_FOOD -> Category(CATEGORY_FOOD, R.drawable.ic_category_food, R.color.categ_color_food)
                CATEGORY_FAST_FOOD -> Category(CATEGORY_FAST_FOOD, R.drawable.ic_category_fast_food, R.color.categ_color_fast_food)
                CATEGORY_CLOTHES -> Category(CATEGORY_CLOTHES, R.drawable.ic_category_clothes, R.color.categ_color_clothes)
                CATEGORY_VEHICLES -> Category(CATEGORY_VEHICLES, R.drawable.ic_category_vehicle, R.color.categ_color_vehicles)
                CATEGORY_VEHICLE_MAINTENANCE -> Category(CATEGORY_VEHICLE_MAINTENANCE, R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance)
                CATEGORY_CONCERTS -> Category(CATEGORY_CONCERTS, R.drawable.ic_category_concerts, R.color.categ_color_concerts)
                CATEGORY_HEALTH -> Category(CATEGORY_HEALTH, R.drawable.ic_category_health, R.color.categ_color_health)
                CATEGORY_SALARY -> Category(CATEGORY_SALARY, R.drawable.ic_category_salary, R.color.categ_color_salary)
                CATEGORY_STUDIES -> Category(CATEGORY_STUDIES, R.drawable.ic_category_particular_studies, R.color.categ_color_studies)
                CATEGORY_MEDICINE -> Category(CATEGORY_MEDICINE, R.drawable.ic_category_medicine, R.color.categ_color_medicine)
                CATEGORY_HOBBIES -> Category(CATEGORY_HOBBIES, R.drawable.ic_category_hobbies, R.color.categ_color_hobbies)
                CATEGORY_ART_PHOTO -> Category(CATEGORY_ART_PHOTO, R.drawable.ic_category_painting_drawing_and_photography, R.color.categ_color_painting_drawing_photos)
                CATEGORY_INVESTMENT -> Category(CATEGORY_INVESTMENT, R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances)
                CATEGORY_MISSING -> Category(CATEGORY_MISSING, R.drawable.ic_other_generic, R.color.categ_color_other)
                else -> Category(name)
            }
        }
    }
}
