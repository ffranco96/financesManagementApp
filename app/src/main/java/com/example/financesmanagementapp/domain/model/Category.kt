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
            return if (category == WITHOUT_CATEGORY) displayName else {
                val prettyParent = category
                    .split("_")
                    .joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercase) }
                return "$prettyParent · $displayName"
            }
        }

    companion object {
        const val CATEGORY_HOUSE_AND_HOME = "HOUSE_AND_HOME"
        const val CATEGORY_SERVICES = "SERVICES"
        const val CATEGORY_TECHNOLOGY = "TECHNOLOGY"
        const val CATEGORY_OTHERS = "OTHERS"
        const val CATEGORY_FINANCIAL_EXPENSES = "FINANCIAL_EXPENSES"
        const val CATEGORY_FOOD_AND_DRINKS = "FOOD_AND_DRINKS"
        const val CATEGORY_INCOME = "INCOME"
        const val CATEGORY_INVESTMENTS = "INVESTMENTS"
        const val CATEGORY_LEISURE = "LEISURE"
        const val CATEGORY_DEBT = "DEBT"
        const val CATEGORY_SHOPS = "SHOPS"
        const val CATEGORY_PETS = "PETS"
        const val CATEGORY_TRANSPORTATION = "TRANSPORTATION"
        const val CATEGORY_HEALTH = "HEALTH"
        const val CATEGORY_VEHICLE = "VEHICLE"
        const val WITHOUT_CATEGORY = "WITHOUT_CATEGORY"

        const val SUBCATEGORY_INSURANCE = "INSURANCE"
        const val SUBCATEGORY_MAINTENANCE = "MAINTENANCE"
        const val SUBCATEGORY_MORTGAGE = "MORTGAGE"
        const val SUBCATEGORY_RENT = "RENT"
        const val SUBCATEGORY_TOOLS = "TOOLS"
        const val SUBCATEGORY_GARDEN = "GARDEN"
        const val SUBCATEGORY_OTHERS = "OTHERS"
        const val SUBCATEGORY_WATER = "WATER"
        const val SUBCATEGORY_LIGHT_GAS_AND_ENERGY = "LIGHT_GAS_AND_ENERGY"
        const val SUBCATEGORY_INTERNET = "INTERNET"
        const val SUBCATEGORY_STREAMING = "STREAMING"
        const val SUBCATEGORY_MUNICIPAL_TAX = "MUNICIPAL_TAX"
        const val SUBCATEGORY_EXPENSES = "EXPENSES"
        const val SUBCATEGORY_MOBILE_TELEPHONY = "MOBILE_TELEPHONY"
        const val SUBCATEGORY_SOFTWARE = "SOFTWARE"
        const val SUBCATEGORY_MAIL_AND_SHIPMENTS = "MAIL_AND_SHIPMENTS"
        const val SUBCATEGORY_LOTERY_CASINO = "LOTERY_CASINO"
        const val SUBCATEGORY_MISSING = "MISSING"
        const val SUBCATEGORY_ASSESMENT = "ASSESMENT"
        const val SUBCATEGORY_CHARGES = "CHARGES"
        const val SUBCATEGORY_FINES = "FINES"
        const val SUBCATEGORY_INSURANCES = "INSURANCES"
        const val SUBCATEGORY_LOAN_INTERESTS = "LOAN_INTERESTS"
        const val SUBCATEGORY_COFEE_RESTAURANT = "COFEE_RESTAURANT"
        const val SUBCATEGORY_MARKET = "MARKET"
        const val SUBCATEGORY_FAST_FOOD = "FAST_FOOD"
        const val SUBCATEGORY_PENSIONS = "PENSIONS"
        const val SUBCATEGORY_CHECKS_COUPONS = "CHECKS_COUPONS"
        const val SUBCATEGORY_SCHOLARSHIPS = "SCHOLARSHIPS"
        const val SUBCATEGORY_INTERESTS_AND_INCOMES = "INTERESTS_AND_INCOMES"
        const val SUBCATEGORY_RENTALS = "RENTALS"
        const val SUBCATEGORY_PRODUCTS_SALE = "PRODUCTS_SALE"
        const val SUBCATEGORY_SALARY = "SALARY"
        const val SUBCATEGORY_SOCIAL_BENEFITS = "SOCIAL_BENEFITS"
        const val SUBCATEGORY_BONUS = "BONUS"
        const val SUBCATEGORY_TRADITIONAL = "TRADITIONAL"
        const val SUBCATEGORY_CRYPTOCURRENCY = "CRYPTOCURRENCY"
        const val SUBCATEGORY_REAL_STATE = "REAL_STATE"
        const val SUBCATEGORY_FOREIGN_CURRENCY = "FOREIGN_CURRENCY"
        const val SUBCATEGORY_MOVABLE_ASSETS = "MOVABLE_ASSETS"
        const val SUBCATEGORY_HARD_ASSETS = "HARD_ASSETS"
        const val SUBCATEGORY_SPORT = "SPORT"
        const val SUBCATEGORY_BOOKS = "BOOKS"
        const val SUBCATEGORY_CONCERTS = "CONCERTS"
        const val SUBCATEGORY_HOBBIES = "HOBBIES"
        const val SUBCATEGORY_TRAVEL_AND_HOLIDAYS = "TRAVEL_AND_HOLIDAYS"
        const val SUBCATEGORY_CINEMA_AND_THEATER = "CINEMA_AND_THEATER"
        const val SUBCATEGORY_EDUCATION_AND_DEVELOPMENT = "EDUCATION_AND_DEVELOPMENT"
        const val SUBCATEGORY_CHARITY = "CHARITY"
        const val SUBCATEGORY_MUSEAMS_AND_CULTURE = "MUSEAMS_AND_CULTURE"
        const val SUBCATEGORY_ALCOHOL_TOBACCO = "ALCOHOL_TOBACCO"
        const val SUBCATEGORY_LOANS = "LOANS"
        const val SUBCATEGORY_APPLIANCES = "APPLIANCES"
        const val SUBCATEGORY_GIFTS = "GIFTS"
        const val SUBCATEGORY_ACCESSORIES = "ACCESSORIES"
        const val SUBCATEGORY_PERSONAL_CARE = "PERSONAL_CARE"
        const val SUBCATEGORY_CLOTHES_AND_SHOES = "CLOTHES_AND_SHOES"
        const val SUBCATEGORY_VET = "VET"
        const val SUBCATEGORY_FOOD = "FOOD"
        const val SUBCATEGORY_DOG_WALKER = "DOG_WALKER"
        const val SUBCATEGORY_HOLIDAY = "HOLIDAY"
        const val SUBCATEGORY_PUBLIC = "PUBLIC"
        const val SUBCATEGORY_TAXI = "TAXI"
        const val SUBCATEGORY_HEALTH_INSURANCE = "HEALTH_INSURANCE"
        const val SUBCATEGORY_MEDICAL_SUPPLIES = "MEDICAL_SUPPLIES"
        const val SUBCATEGORY_BEAUTY = "BEAUTY"
        const val SUBCATEGORY_STUDIES = "STUDIES"
        const val SUBCATEGORY_MEDICINE = "MEDICINE"
        const val SUBCATEGORY_FUEL = "FUEL"
        const val SUBCATEGORY_PARKING = "PARKING"
        const val SUBCATEGORY_RENTAL = "RENTAL"
        const val SUBCATEGORY_MANTAINANCE = "MANTAINANCE"
        const val SUBCATEGORY_TOLLS = "TOLLS"

        /**
         * Version of the [ALL_CATEGORIES] taxonomy. Bump this whenever the list changes so
         * [com.example.financesmanagementapp.domain.usecase.InitializeConfigUseCase] knows to
         * reseed the categories stored in DataStore.
         */
        const val CATEGORIES_VERSION = 2

        val ALL_CATEGORIES: List<Category> = listOf(
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_INSURANCE, "Seguro del hogar", colorIcon = R.color.categ_color_house_and_home_insurance),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_MAINTENANCE, "Mantenimiento y reparaciones", colorIcon = R.color.categ_color_house_and_home_maintenance),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_MORTGAGE, "Hipoteca", colorIcon = R.color.categ_color_house_and_home_mortgage),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_RENT, "Alquiler", colorIcon = R.color.categ_color_house_and_home_rent),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_TOOLS, "Herramientas", colorIcon = R.color.categ_color_house_and_home_tools),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_GARDEN, "Jardín", colorIcon = R.color.categ_color_house_and_home_garden),
            Category(CATEGORY_HOUSE_AND_HOME, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_house_and_home_others),
            Category(CATEGORY_SERVICES, SUBCATEGORY_WATER, "Agua", colorIcon = R.color.categ_color_services_water),
            Category(CATEGORY_SERVICES, SUBCATEGORY_LIGHT_GAS_AND_ENERGY, "Luz, gas y energia", colorIcon = R.color.categ_color_services_light_gas_and_energy),
            Category(CATEGORY_SERVICES, SUBCATEGORY_INTERNET, "Internet", colorIcon = R.color.categ_color_services_internet),
            Category(CATEGORY_SERVICES, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_services_others),
            Category(CATEGORY_SERVICES, SUBCATEGORY_STREAMING, "Plataformas streaming", colorIcon = R.color.categ_color_services_streaming),
            Category(CATEGORY_SERVICES, SUBCATEGORY_MUNICIPAL_TAX, "Municipal", colorIcon = R.color.categ_color_services_municipal_tax),
            Category(CATEGORY_SERVICES, SUBCATEGORY_EXPENSES, "Expensas", colorIcon = R.color.categ_color_services_expenses),
            Category(CATEGORY_SERVICES, SUBCATEGORY_MOBILE_TELEPHONY, "Telefonia movil", colorIcon = R.color.categ_color_services_mobile_telephony),
            Category(CATEGORY_TECHNOLOGY, SUBCATEGORY_SOFTWARE, "Software, apps y juegos", colorIcon = R.color.categ_color_technology_software),
            Category(CATEGORY_OTHERS, SUBCATEGORY_MAIL_AND_SHIPMENTS, "Correo y envios", colorIcon = R.color.categ_color_others_mail_and_shipments),
            Category(CATEGORY_OTHERS, SUBCATEGORY_LOTERY_CASINO, "Loteria y casino", colorIcon = R.color.categ_color_others_lotery_casino),
            Category(CATEGORY_OTHERS, SUBCATEGORY_MISSING, "Faltantes", R.drawable.ic_other_generic, R.color.categ_color_other),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_ASSESMENT, "Asesoria financiera", colorIcon = R.color.categ_color_financial_expenses_assesment),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_CHARGES, "Cargos y comisiones", colorIcon = R.color.categ_color_financial_expenses_charges),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_FINES, "Multas", colorIcon = R.color.categ_color_financial_expenses_fines),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_INSURANCES, "Seguros", colorIcon = R.color.categ_color_financial_expenses_insurances),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_LOAN_INTERESTS, "Intereses de prestamos", colorIcon = R.color.categ_color_financial_expenses_loan_interests),
            Category(CATEGORY_FINANCIAL_EXPENSES, SUBCATEGORY_OTHERS, "Otros", R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances),
            Category(CATEGORY_FOOD_AND_DRINKS, SUBCATEGORY_COFEE_RESTAURANT, "Cafeteria y restaurant", colorIcon = R.color.categ_color_food_and_drinks_cofee_restaurant),
            Category(CATEGORY_FOOD_AND_DRINKS, SUBCATEGORY_MARKET, "Supermercado y almacen", colorIcon = R.color.categ_color_food_and_drinks_market),
            Category(CATEGORY_FOOD_AND_DRINKS, SUBCATEGORY_FAST_FOOD, "Comida rapida y delivery", R.drawable.ic_category_fast_food, R.color.categ_color_fast_food),
            Category(CATEGORY_FOOD_AND_DRINKS, SUBCATEGORY_OTHERS, "Otros", R.drawable.ic_category_food, R.color.categ_color_food),
            Category(CATEGORY_INCOME, SUBCATEGORY_PENSIONS, "Pensiones", colorIcon = R.color.categ_color_income_pensions),
            Category(CATEGORY_INCOME, SUBCATEGORY_CHECKS_COUPONS, "Cheques y cupones", colorIcon = R.color.categ_color_income_checks_coupons),
            Category(CATEGORY_INCOME, SUBCATEGORY_SCHOLARSHIPS, "Becas", colorIcon = R.color.categ_color_income_scholarships),
            Category(CATEGORY_INCOME, SUBCATEGORY_INTERESTS_AND_INCOMES, "Intereses y dividendos", colorIcon = R.color.categ_color_income_interests_and_incomes),
            Category(CATEGORY_INCOME, SUBCATEGORY_RENTALS, "Alquileres", colorIcon = R.color.categ_color_income_rentals),
            Category(CATEGORY_INCOME, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_income_others),
            Category(CATEGORY_INCOME, SUBCATEGORY_PRODUCTS_SALE, "Venta de productos", colorIcon = R.color.categ_color_income_products_sale),
            Category(CATEGORY_INCOME, SUBCATEGORY_SALARY, "Sueldo", R.drawable.ic_category_salary, R.color.categ_color_salary),
            Category(CATEGORY_INCOME, SUBCATEGORY_SOCIAL_BENEFITS, "Plan estatal", colorIcon = R.color.categ_color_income_social_benefits),
            Category(CATEGORY_INCOME, SUBCATEGORY_BONUS, "Bonos, aguinaldo", colorIcon = R.color.categ_color_income_bonus),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_TRADITIONAL, "Inversiones financieras tradicionales", colorIcon = R.color.categ_color_investments_traditional),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_CRYPTOCURRENCY, "Inversiones criptomonedas", colorIcon = R.color.categ_color_investments_cryptocurrency),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_REAL_STATE, "Bienes inmuebles", colorIcon = R.color.categ_color_investments_real_state),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_FOREIGN_CURRENCY, "Moneda extranjera", colorIcon = R.color.categ_color_investments_foreign_currency),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_MOVABLE_ASSETS, "Bienes muebles", colorIcon = R.color.categ_color_investments_movable_assets),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_investments_others),
            Category(CATEGORY_INVESTMENTS, SUBCATEGORY_HARD_ASSETS, "Activos duros", colorIcon = R.color.categ_color_investments_hard_assets),
            Category(CATEGORY_LEISURE, SUBCATEGORY_SPORT, "Deporte", colorIcon = R.color.categ_color_leisure_sport),
            Category(CATEGORY_LEISURE, SUBCATEGORY_BOOKS, "Libros", colorIcon = R.color.categ_color_leisure_books),
            Category(CATEGORY_LEISURE, SUBCATEGORY_CONCERTS, "Recitales", R.drawable.ic_category_concerts, R.color.categ_color_concerts),
            Category(CATEGORY_LEISURE, SUBCATEGORY_HOBBIES, "Hobbies", R.drawable.ic_category_hobbies, R.color.categ_color_hobbies),
            Category(CATEGORY_LEISURE, SUBCATEGORY_TRAVEL_AND_HOLIDAYS, "Viajes y vacaciones", colorIcon = R.color.categ_color_leisure_travel_and_holidays),
            Category(CATEGORY_LEISURE, SUBCATEGORY_CINEMA_AND_THEATER, "Cine y teatro", colorIcon = R.color.categ_color_leisure_cinema_and_theater),
            Category(CATEGORY_LEISURE, SUBCATEGORY_EDUCATION_AND_DEVELOPMENT, "Educacion y desarrollo", colorIcon = R.color.categ_color_leisure_education_and_development),
            Category(CATEGORY_LEISURE, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_leisure_others),
            Category(CATEGORY_LEISURE, SUBCATEGORY_CHARITY, "Caridad", colorIcon = R.color.categ_color_leisure_charity),
            Category(CATEGORY_LEISURE, SUBCATEGORY_MUSEAMS_AND_CULTURE, "Museos y cultura", colorIcon = R.color.categ_color_leisure_museams_and_culture),
            Category(CATEGORY_LEISURE, SUBCATEGORY_ALCOHOL_TOBACCO, "Alcohol y tabaco", colorIcon = R.color.categ_color_leisure_alcohol_tobacco),
            Category(CATEGORY_DEBT, SUBCATEGORY_LOANS, "Prestamos", colorIcon = R.color.categ_color_debt_loans),
            Category(CATEGORY_DEBT, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_debt_others),
            Category(CATEGORY_SHOPS, SUBCATEGORY_APPLIANCES, "Electrodomesticos", colorIcon = R.color.categ_color_shops_appliances),
            Category(CATEGORY_SHOPS, SUBCATEGORY_GIFTS, "Regalos", colorIcon = R.color.categ_color_shops_gifts),
            Category(CATEGORY_SHOPS, SUBCATEGORY_ACCESSORIES, "Accesorios", colorIcon = R.color.categ_color_shops_accessories),
            Category(CATEGORY_SHOPS, SUBCATEGORY_PERSONAL_CARE, "Perfumeria", colorIcon = R.color.categ_color_shops_personal_care),
            Category(CATEGORY_SHOPS, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_shops_others),
            Category(CATEGORY_SHOPS, SUBCATEGORY_CLOTHES_AND_SHOES, "Ropa y calzado", R.drawable.ic_category_clothes, R.color.categ_color_clothes),
            Category(CATEGORY_PETS, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_pets_others),
            Category(CATEGORY_PETS, SUBCATEGORY_VET, "Veterinario", colorIcon = R.color.categ_color_pets_vet),
            Category(CATEGORY_PETS, SUBCATEGORY_FOOD, "Alimento", colorIcon = R.color.categ_color_pets_food),
            Category(CATEGORY_PETS, SUBCATEGORY_DOG_WALKER, "Paseador (perros)", colorIcon = R.color.categ_color_pets_dog_walker),
            Category(CATEGORY_TRANSPORTATION, SUBCATEGORY_HOLIDAY, "Viajes de vacaciones", colorIcon = R.color.categ_color_transportation_holiday),
            Category(CATEGORY_TRANSPORTATION, SUBCATEGORY_OTHERS, "Otros", colorIcon = R.color.categ_color_transportation_others),
            Category(CATEGORY_TRANSPORTATION, SUBCATEGORY_PUBLIC, "Transporte publico", colorIcon = R.color.categ_color_transportation_public),
            Category(CATEGORY_TRANSPORTATION, SUBCATEGORY_TAXI, "Taxi, remis y otras empresas", colorIcon = R.color.categ_color_transportation_taxi),
            Category(CATEGORY_HEALTH, SUBCATEGORY_HEALTH_INSURANCE, "Prepaga y obra social", colorIcon = R.color.categ_color_health_health_insurance),
            Category(CATEGORY_HEALTH, SUBCATEGORY_MEDICAL_SUPPLIES, "Insumos medicos", colorIcon = R.color.categ_color_health_medical_supplies),
            Category(CATEGORY_HEALTH, SUBCATEGORY_BEAUTY, "Bienestar y belleza", colorIcon = R.color.categ_color_health_beauty),
            Category(CATEGORY_HEALTH, SUBCATEGORY_STUDIES, "Estudios", R.drawable.ic_category_particular_studies, R.color.categ_color_studies),
            Category(CATEGORY_HEALTH, SUBCATEGORY_MEDICINE, "Medicación", R.drawable.ic_category_medicine, R.color.categ_color_medicine),
            Category(CATEGORY_HEALTH, SUBCATEGORY_OTHERS, "Otros", R.drawable.ic_category_health, R.color.categ_color_health),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_FUEL, "Combustible", colorIcon = R.color.categ_color_vehicle_fuel),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_INSURANCE, "Seguro del vehiculo", colorIcon = R.color.categ_color_vehicle_insurance),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_OTHERS, "Otros", R.drawable.ic_category_vehicle, R.color.categ_color_vehicles),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_PARKING, "Estacionamiento", colorIcon = R.color.categ_color_vehicle_parking),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_RENTAL, "Alquiler de vehiculos", colorIcon = R.color.categ_color_vehicle_rental),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_MANTAINANCE, "Mantenimiento del vehiculo", R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance),
            Category(CATEGORY_VEHICLE, SUBCATEGORY_TOLLS, "Peajes", colorIcon = R.color.categ_color_vehicle_tolls),
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
