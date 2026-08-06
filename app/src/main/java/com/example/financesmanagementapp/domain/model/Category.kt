package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.R

/**
 * Data class representing the category of a movement in the app.
 * A category is identified by a parent [category] (e.g. "VEHICLE") and a [subcategory]
 * (e.g. "FUEL"), both stable constants used for lookup/persistence. [displayName] is
 * the text shown in the UI and is not used as a key.
 */
data class Category(
    var category: String = WITHOUT_CATEGORY,
    var subcategory: String = "",
    var displayName: String = "Sin categoría",
    var iconRsc: Int = R.drawable.ic_other_generic,
    var colorIcon: Int = R.color.categ_color_other,
    var details: String = ""
) {
    /**
     * Readable label combining the parent category and [displayName] (e.g.
     * "House And Home · Seguro del hogar"), since [displayName] alone isn't unique across
     * parent categories. Purely for UI presentation, not persisted.
     */
    val displayLabel: String
        get() {
            val prettyParent = category
                .split("_")
                .joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercase) }
            return "$prettyParent · $displayName"
        }

    companion object {
        const val HOUSE_AND_HOME = "HOUSE_AND_HOME"
        const val SERVICES = "SERVICES"
        const val TECHNOLOGY = "TECHNOLOGY"
        const val OTHERS = "OTHERS"
        const val FINANCIAL_EXPENSES = "FINANCIAL_EXPENSES"
        const val FOOD_AND_DRINKS = "FOOD_AND_DRINKS"
        const val INCOME = "INCOME"
        const val INVESTMENTS = "INVESTMENTS"
        const val LEISURE = "LEISURE"
        const val DEBT = "DEBT"
        const val SHOPS = "SHOPS"
        const val PETS = "PETS"
        const val TRANSPORTATION = "TRANSPORTATION"
        const val HEALTH = "HEALTH"
        const val VEHICLE = "VEHICLE"
        const val WITHOUT_CATEGORY = "WITHOUT_CATEGORY"

        /**
         * Version of the [ALL_CATEGORIES] taxonomy. Bump this whenever the list changes so
         * [com.example.financesmanagementapp.domain.usecase.InitializeConfigUseCase] knows to
         * reseed the categories stored in DataStore.
         */
        const val CATEGORIES_VERSION = 2

        val ALL_CATEGORIES: List<Category> = listOf(
            Category(HOUSE_AND_HOME, "INSURANCE", "Seguro del hogar", colorIcon = R.color.categ_color_house_and_home_insurance),
            Category(HOUSE_AND_HOME, "MAINTENANCE", "Mantenimiento y reparaciones", colorIcon = R.color.categ_color_house_and_home_maintenance),
            Category(HOUSE_AND_HOME, "MORTGAGE", "Hipoteca", colorIcon = R.color.categ_color_house_and_home_mortgage),
            Category(HOUSE_AND_HOME, "RENT", "Alquiler", colorIcon = R.color.categ_color_house_and_home_rent),
            Category(HOUSE_AND_HOME, "TOOLS", "Herramientas", colorIcon = R.color.categ_color_house_and_home_tools),
            Category(HOUSE_AND_HOME, "GARDEN", "Jardín", colorIcon = R.color.categ_color_house_and_home_garden),
            Category(HOUSE_AND_HOME, "OTHERS", "Otros", colorIcon = R.color.categ_color_house_and_home_others),
            Category(SERVICES, "WATER", "Agua", colorIcon = R.color.categ_color_services_water),
            Category(SERVICES, "LIGHT_GAS_AND_ENERGY", "Luz, gas y energia", colorIcon = R.color.categ_color_services_light_gas_and_energy),
            Category(SERVICES, "INTERNET", "Internet", colorIcon = R.color.categ_color_services_internet),
            Category(SERVICES, "OTHERS", "Otros", colorIcon = R.color.categ_color_services_others),
            Category(SERVICES, "STREAMING", "Plataformas streaming", colorIcon = R.color.categ_color_services_streaming),
            Category(SERVICES, "MUNICIPAL_TAX", "Municipal", colorIcon = R.color.categ_color_services_municipal_tax),
            Category(SERVICES, "EXPENSES", "Expensas", colorIcon = R.color.categ_color_services_expenses),
            Category(SERVICES, "MOBILE_TELEPHONY", "Telefonia movil", colorIcon = R.color.categ_color_services_mobile_telephony),
            Category(TECHNOLOGY, "SOFTWARE", "Software, apps y juegos", colorIcon = R.color.categ_color_technology_software),
            Category(OTHERS, "MAIL_AND_SHIPMENTS", "Correo y envios", colorIcon = R.color.categ_color_others_mail_and_shipments),
            Category(OTHERS, "LOTERY_CASINO", "Loteria y casino", colorIcon = R.color.categ_color_others_lotery_casino),
            Category(OTHERS, "MISSING", "Faltantes", R.drawable.ic_other_generic, R.color.categ_color_other),
            Category(FINANCIAL_EXPENSES, "ASSESMENT", "Asesoria financiera", colorIcon = R.color.categ_color_financial_expenses_assesment),
            Category(FINANCIAL_EXPENSES, "CHARGES", "Cargos y comisiones", colorIcon = R.color.categ_color_financial_expenses_charges),
            Category(FINANCIAL_EXPENSES, "FINES", "Multas", colorIcon = R.color.categ_color_financial_expenses_fines),
            Category(FINANCIAL_EXPENSES, "INSURANCES", "Seguros", colorIcon = R.color.categ_color_financial_expenses_insurances),
            Category(FINANCIAL_EXPENSES, "LOAN_INTERESTS", "Intereses de prestamos", colorIcon = R.color.categ_color_financial_expenses_loan_interests),
            Category(FINANCIAL_EXPENSES, "OTHERS", "Otros", R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances),
            Category(FOOD_AND_DRINKS, "COFEE_RESTAURANT", "Cafeteria y restaurant", colorIcon = R.color.categ_color_food_and_drinks_cofee_restaurant),
            Category(FOOD_AND_DRINKS, "MARKET", "Supermercado y almacen", colorIcon = R.color.categ_color_food_and_drinks_market),
            Category(FOOD_AND_DRINKS, "FAST_FOOD", "Comida rapida y delivery", R.drawable.ic_category_fast_food, R.color.categ_color_fast_food),
            Category(FOOD_AND_DRINKS, "OTHERS", "Otros", R.drawable.ic_category_food, R.color.categ_color_food),
            Category(INCOME, "PENSIONS", "Pensiones", colorIcon = R.color.categ_color_income_pensions),
            Category(INCOME, "CHECKS_COUPONS", "Cheques y cupones", colorIcon = R.color.categ_color_income_checks_coupons),
            Category(INCOME, "SCHOLARSHIPS", "Becas", colorIcon = R.color.categ_color_income_scholarships),
            Category(INCOME, "INTERESTS_AND_INCOMES", "Intereses y dividendos", colorIcon = R.color.categ_color_income_interests_and_incomes),
            Category(INCOME, "RENTALS", "Alquileres", colorIcon = R.color.categ_color_income_rentals),
            Category(INCOME, "OTHERS", "Otros", colorIcon = R.color.categ_color_income_others),
            Category(INCOME, "PRODUCTS_SALE", "Venta de productos", colorIcon = R.color.categ_color_income_products_sale),
            Category(INCOME, "SALARY", "Sueldo", R.drawable.ic_category_salary, R.color.categ_color_salary),
            Category(INCOME, "SOCIAL_BENEFITS", "Plan estatal", colorIcon = R.color.categ_color_income_social_benefits),
            Category(INCOME, "BONUS", "Bonos, aguinaldo", colorIcon = R.color.categ_color_income_bonus),
            Category(INVESTMENTS, "TRADITIONAL", "Inversiones financieras tradicionales", colorIcon = R.color.categ_color_investments_traditional),
            Category(INVESTMENTS, "CRYPTOCURRENCY", "Inversiones criptomonedas", colorIcon = R.color.categ_color_investments_cryptocurrency),
            Category(INVESTMENTS, "REAL_STATE", "Bienes inmuebles", colorIcon = R.color.categ_color_investments_real_state),
            Category(INVESTMENTS, "FOREIGN_CURRENCY", "Moneda extranjera", colorIcon = R.color.categ_color_investments_foreign_currency),
            Category(INVESTMENTS, "MOVABLE_ASSETS", "Bienes muebles", colorIcon = R.color.categ_color_investments_movable_assets),
            Category(INVESTMENTS, "OTHERS", "Otros", colorIcon = R.color.categ_color_investments_others),
            Category(INVESTMENTS, "HARD_ASSETS", "Activos duros", colorIcon = R.color.categ_color_investments_hard_assets),
            Category(LEISURE, "SPORT", "Deporte", colorIcon = R.color.categ_color_leisure_sport),
            Category(LEISURE, "BOOKS", "Libros", colorIcon = R.color.categ_color_leisure_books),
            Category(LEISURE, "CONCERTS", "Recitales", R.drawable.ic_category_concerts, R.color.categ_color_concerts),
            Category(LEISURE, "HOBBIES", "Hobbies", R.drawable.ic_category_hobbies, R.color.categ_color_hobbies),
            Category(LEISURE, "TRAVEL_AND_HOLIDAYS", "Viajes y vacaciones", colorIcon = R.color.categ_color_leisure_travel_and_holidays),
            Category(LEISURE, "CINEMA_AND_THEATER", "Cine y teatro", colorIcon = R.color.categ_color_leisure_cinema_and_theater),
            Category(LEISURE, "EDUCATION_AND_DEVELOPMENT", "Educacion y desarrollo", colorIcon = R.color.categ_color_leisure_education_and_development),
            Category(LEISURE, "OTHERS", "Otros", colorIcon = R.color.categ_color_leisure_others),
            Category(LEISURE, "CHARITY", "Caridad", colorIcon = R.color.categ_color_leisure_charity),
            Category(LEISURE, "MUSEAMS_AND_CULTURE", "Museos y cultura", colorIcon = R.color.categ_color_leisure_museams_and_culture),
            Category(LEISURE, "ALCOHOL_TOBACCO", "Alcohol y tabaco", colorIcon = R.color.categ_color_leisure_alcohol_tobacco),
            Category(DEBT, "LOANS", "Prestamos", colorIcon = R.color.categ_color_debt_loans),
            Category(DEBT, "OTHERS", "Otros", colorIcon = R.color.categ_color_debt_others),
            Category(SHOPS, "APPLIANCES", "Electrodomesticos", colorIcon = R.color.categ_color_shops_appliances),
            Category(SHOPS, "GIFTS", "Regalos", colorIcon = R.color.categ_color_shops_gifts),
            Category(SHOPS, "ACCESSORIES", "Accesorios", colorIcon = R.color.categ_color_shops_accessories),
            Category(SHOPS, "PERSONAL_CARE", "Perfumeria", colorIcon = R.color.categ_color_shops_personal_care),
            Category(SHOPS, "OTHERS", "Otros", colorIcon = R.color.categ_color_shops_others),
            Category(SHOPS, "CLOTHES_AND_SHOES", "Ropa y calzado", R.drawable.ic_category_clothes, R.color.categ_color_clothes),
            Category(PETS, "OTHERS", "Otros", colorIcon = R.color.categ_color_pets_others),
            Category(PETS, "VET", "Veterinario", colorIcon = R.color.categ_color_pets_vet),
            Category(PETS, "FOOD", "Alimento", colorIcon = R.color.categ_color_pets_food),
            Category(PETS, "DOG_WALKER", "Paseador (perros)", colorIcon = R.color.categ_color_pets_dog_walker),
            Category(TRANSPORTATION, "HOLIDAY", "Viajes de vacaciones", colorIcon = R.color.categ_color_transportation_holiday),
            Category(TRANSPORTATION, "OTHERS", "Otros", colorIcon = R.color.categ_color_transportation_others),
            Category(TRANSPORTATION, "PUBLIC", "Transporte publico", colorIcon = R.color.categ_color_transportation_public),
            Category(TRANSPORTATION, "TAXI", "Taxi, remis y otras empresas", colorIcon = R.color.categ_color_transportation_taxi),
            Category(HEALTH, "HEALTH_INSURANCE", "Prepaga y obra social", colorIcon = R.color.categ_color_health_health_insurance),
            Category(HEALTH, "MEDICAL_SUPPLIES", "Insumos medicos", colorIcon = R.color.categ_color_health_medical_supplies),
            Category(HEALTH, "BEAUTY", "Bienestar y belleza", colorIcon = R.color.categ_color_health_beauty),
            Category(HEALTH, "STUDIES", "Estudios", R.drawable.ic_category_particular_studies, R.color.categ_color_studies),
            Category(HEALTH, "MEDICINE", "Medicación", R.drawable.ic_category_medicine, R.color.categ_color_medicine),
            Category(HEALTH, "OTHERS", "Otros", R.drawable.ic_category_health, R.color.categ_color_health),
            Category(VEHICLE, "FUEL", "Combustible", colorIcon = R.color.categ_color_vehicle_fuel),
            Category(VEHICLE, "INSURANCE", "Seguro del vehiculo", colorIcon = R.color.categ_color_vehicle_insurance),
            Category(VEHICLE, "OTHERS", "Otros", R.drawable.ic_category_vehicle, R.color.categ_color_vehicles),
            Category(VEHICLE, "PARKING", "Estacionamiento", colorIcon = R.color.categ_color_vehicle_parking),
            Category(VEHICLE, "RENTAL", "Alquiler de vehiculos", colorIcon = R.color.categ_color_vehicle_rental),
            Category(VEHICLE, "MANTAINANCE", "Mantenimiento del vehiculo", R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance),
            Category(VEHICLE, "TOLLS", "Peajes", colorIcon = R.color.categ_color_vehicle_tolls),
            Category(WITHOUT_CATEGORY, "", "Sin categoría")
        )

        /**
         * Looks up the complete [Category] (with icon/color) for a given category/subcategory
         * pair. Falls back to a bare [Category] with default icon/color if the pair isn't found
         * in [ALL_CATEGORIES] (e.g. stale data from a previous taxonomy).
         */
        fun fromCategoryAndSubcategory(category: String, subcategory: String): Category {
            return ALL_CATEGORIES.find { it.category == category && it.subcategory == subcategory }
                ?: Category(category, subcategory)
        }
    }
}
