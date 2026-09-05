package com.example.financesmanagementapp.domain.model

import com.example.financesmanagementapp.R

/**
 * Parent category of a movement. [name] (e.g. "CATEGORY_HOUSE_AND_HOME") is the stable key used
 * for persistence (DB columns, CSV export/import); [label] is the Spanish label shown in the UI.
 */
enum class CategoryName(val label: String) {
    CATEGORY_HOUSE_AND_HOME("Casa y Hogar"),
    CATEGORY_SERVICES("Servicios"),
    CATEGORY_TECHNOLOGY("Tecnología"),
    CATEGORY_OTHERS("Otros"),
    CATEGORY_FINANCIAL_EXPENSES("Gastos Financieros"),
    CATEGORY_FOOD_AND_DRINKS("Comida y Bebidas"),
    CATEGORY_INCOME("Ingresos"),
    CATEGORY_INVESTMENTS("Inversiones"),
    CATEGORY_LEISURE("Ocio"),
    CATEGORY_EDUCATION_AND_DEVELOPMENT("Educación y desarrollo"),
    CATEGORY_SPORTS("Deporte"),
    CATEGORY_TRAVEL("Viajes"),
    CATEGORY_DEBT("Deudas"),
    CATEGORY_SHOPS("Compras"),
    CATEGORY_PETS("Mascotas"),
    CATEGORY_TRANSPORTATION("Transporte"),
    CATEGORY_HEALTH("Salud"),
    CATEGORY_VEHICLE("Vehículo"),
    WITHOUT_CATEGORY("Sin categoría");

    companion object {
        /** Parses a persisted/CSV value back into a [CategoryName], defaulting to [WITHOUT_CATEGORY] for unknown or stale values. */
        fun fromRaw(raw: String): CategoryName = entries.find { it.name == raw } ?: WITHOUT_CATEGORY
    }
}

/**
 * Subcategory of a movement. [name] (e.g. "SUBCATEGORY_FUEL") is the stable key used for
 * persistence; [label] is the Spanish label shown in the UI — the single source of truth for
 * [Category.displayName] when [Category.ALL_CATEGORIES] is built.
 *
 * Several subcategories are shared by more than one parent category (e.g. [SUBCATEGORY_INSURANCE],
 * [SUBCATEGORY_MAINTENANCE], [SUBCATEGORY_HOTELS].
 * A shared subcategory carries a single generic label; the parent category's own [CategoryName.label]
 * (via [Category.displayLabel]) is what will make the difference when showing the labels in the UI.
 */
enum class SubcategoryName(val label: String) {
    SUBCATEGORY_INSURANCE("Seguro"),
    SUBCATEGORY_MAINTENANCE("Mantenimiento y reparaciones"),
    SUBCATEGORY_MORTGAGE("Hipoteca"),
    SUBCATEGORY_RENT("Alquiler"),
    SUBCATEGORY_TOOLS("Herramientas"),
    SUBCATEGORY_GARDEN("Jardín"),
    SUBCATEGORY_OTHERS("Otros"),
    SUBCATEGORY_WATER("Agua"),
    SUBCATEGORY_LIGHT_GAS_AND_ENERGY("Luz, gas y energía"),
    SUBCATEGORY_INTERNET("Internet"),
    SUBCATEGORY_STREAMING("Plataformas streaming"),
    SUBCATEGORY_MUNICIPAL_TAX("Municipal"),
    SUBCATEGORY_EXPENSES("Expensas"),
    SUBCATEGORY_MOBILE_TELEPHONY("Telefonía móvil"),
    SUBCATEGORY_SOFTWARE_AND_GAMES("Software, apps y juegos"),
    SUBCATEGORY_MAIL_AND_SHIPMENTS("Correo y envíos"),
    SUBCATEGORY_LOTERY_CASINO("Lotería y casino"),
    SUBCATEGORY_MISSING("Faltantes"),
    SUBCATEGORY_ADMINISTRATIVE("Trámites y gestiones"),
    SUBCATEGORY_ASSESMENT("Asesoría financiera"),
    SUBCATEGORY_CHARGES("Cargos y comisiones"),
    SUBCATEGORY_FINES("Multas"),
    SUBCATEGORY_INSURANCES("Seguros"),
    SUBCATEGORY_LOAN_INTERESTS("Intereses de préstamos"),
    SUBCATEGORY_LOAN("Préstamos"),
    SUBCATEGORY_COFFEE_RESTAURANT("Cafetería y restaurant"),
    SUBCATEGORY_MARKET("Supermercado y almacén"),
    SUBCATEGORY_FAST_FOOD("Comida rápida y delivery"),
    SUBCATEGORY_PENSIONS("Pensiones"),
    SUBCATEGORY_CHECKS_COUPONS("Cheques y cupones"),
    SUBCATEGORY_SCHOLARSHIPS("Becas"),
    SUBCATEGORY_RENTALS("Alquileres"),
    SUBCATEGORY_PRODUCTS_SALE("Venta de productos"),
    SUBCATEGORY_SALARY("Sueldo"),
    SUBCATEGORY_SOCIAL_BENEFITS("Plan estatal"),
    SUBCATEGORY_BONUS("Bonos, aguinaldo"),
    SUBCATEGORY_REFUND_SHARED_EXPENSES("Devolución de gastos compartidos"),
    SUBCATEGORY_EXCHANGE("Cambio de divisas"),
    SUBCATEGORY_TRADITIONAL("Inversiones financieras tradicionales"),
    SUBCATEGORY_CRYPTOCURRENCY("Inversiones criptomonedas"),
    SUBCATEGORY_REAL_STATE("Bienes inmuebles"),
    SUBCATEGORY_FOREIGN_CURRENCY("Moneda extranjera"),
    SUBCATEGORY_MOVABLE_ASSETS("Bienes muebles"),
    SUBCATEGORY_HARD_ASSETS("Activos duros"),
    SUBCATEGORY_BOOKS("Libros"),
    SUBCATEGORY_CONCERTS("Recitales"),
    SUBCATEGORY_EVENTS("Eventos y salidas"),
    SUBCATEGORY_HOTELS("Hoteles y alojamiento"),
    SUBCATEGORY_TRAVEL_TICKETS("Pasajes"),
    SUBCATEGORY_EXCURSIONS("Excursiones y tours"),
    SUBCATEGORY_CINEMA_AND_THEATER("Cine y teatro"),
    SUBCATEGORY_CHARITY("Caridad"),
    SUBCATEGORY_MUSEAMS_AND_CULTURE("Museos y cultura"),
    SUBCATEGORY_ALCOHOL_TOBACCO("Alcohol y tabaco"),
    SUBCATEGORY_PAINTING_DRAWING_AND_PHOTOGRAPHY("Pintura, dibujo y fotografía"),
    SUBCATEGORY_APPLIANCES("Electrodomésticos"),
    SUBCATEGORY_GIFTS("Regalos"),
    SUBCATEGORY_ACCESSORIES("Accesorios"),
    SUBCATEGORY_PERSONAL_CARE("Perfumería"),
    SUBCATEGORY_CLOTHES_AND_SHOES("Ropa y calzado"),
    SUBCATEGORY_DRUGSTORE("Kiosco (golosinas y otros)"),
    SUBCATEGORY_ELECTRONICS("Dispositivos y electrónica"),
    SUBCATEGORY_VET("Veterinario"),
    SUBCATEGORY_FOOD("Comida"),
    SUBCATEGORY_DOG_WALKER("Paseador (perros)"),
    SUBCATEGORY_PUBLIC("Transporte público"),
    SUBCATEGORY_TAXI("Taxi, remis y otras empresas"),
    SUBCATEGORY_HEALTH_INSURANCE("Prepaga y obra social"),
    SUBCATEGORY_MEDICAL_SUPPLIES("Insumos médicos"),
    SUBCATEGORY_BEAUTY("Bienestar y belleza"),
    SUBCATEGORY_STUDIES("Estudios"),
    SUBCATEGORY_MEDICINE("Medicación"),
    SUBCATEGORY_FUEL("Combustible"),
    SUBCATEGORY_PARKING("Estacionamiento"),
    SUBCATEGORY_RENTAL("Alquiler de vehículos"),
    SUBCATEGORY_TOLLS("Peajes"),
    SUBCATEGORY_NONE("");

    companion object {
        /** Parses a persisted/CSV value back into a [SubcategoryName], defaulting to [SUBCATEGORY_NONE] for unknown, blank or stale values. */
        fun fromRaw(raw: String): SubcategoryName = entries.find { it.name == raw } ?: SUBCATEGORY_NONE
    }
}

/**
 * Data class representing the category of a movement in the app.
 * A category is identified by a parent [categoryName] and a [subcategoryName], both enums whose
 * [Enum.name] (e.g. "CATEGORY_VEHICLE", "SUBCATEGORY_FUEL") is the stable key used for
 * lookup/persistence. [displayName] is the text shown in the UI and is not used as a key; when
 * building [ALL_CATEGORIES] it's always set to the owning [SubcategoryName.label], so that label
 * is the single source of truth for the Spanish text.
 */
data class Category(
    var categoryName: CategoryName = CategoryName.WITHOUT_CATEGORY,
    var subcategoryName: SubcategoryName = SubcategoryName.SUBCATEGORY_NONE,
    var displayName: String = "Sin categoría",
    var iconRsc: Int = R.drawable.ic_other_generic,
    var colorCategory: Int = R.color.categ_color_other,
    var isIncome: Boolean = false,
    var details: String = ""
) {
    /**
     * Readable label combining the parent category's Spanish [CategoryName.label] and [displayName]
     * (e.g. "Casa y Hogar · Jardín"), since [displayName] alone isn't unique across parent
     * categories. Purely for UI presentation, not persisted.
     */
    val displayLabel: String
        get() = if (categoryName == CategoryName.WITHOUT_CATEGORY) displayName
        else "${categoryName.label} · $displayName"

    companion object {
        /**
         * Version of the [ALL_CATEGORIES] taxonomy. Bump this whenever the list changes so
         * [com.example.financesmanagementapp.domain.usecase.InitializeConfigUseCase] knows to
         * reseed the categories stored in DataStore.
         */
        const val CATEGORIES_VERSION = 7

        val ALL_CATEGORIES: List<Category> = listOf(
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_INSURANCE, SubcategoryName.SUBCATEGORY_INSURANCE.label, iconRsc = R.drawable.ic_subcategory_house_insurance, colorCategory = R.color.categ_color_house_and_home_insurance),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_MAINTENANCE, SubcategoryName.SUBCATEGORY_MAINTENANCE.label, iconRsc = R.drawable.ic_subcategory_maintenance, colorCategory = R.color.categ_color_house_and_home_maintenance),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_MORTGAGE, SubcategoryName.SUBCATEGORY_MORTGAGE.label, iconRsc = R.drawable.ic_subcategory_mortgage, colorCategory = R.color.categ_color_house_and_home_mortgage),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_RENT, SubcategoryName.SUBCATEGORY_RENT.label, iconRsc = R.drawable.ic_subcategory_rent, colorCategory = R.color.categ_color_house_and_home_rent),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_TOOLS, SubcategoryName.SUBCATEGORY_TOOLS.label, iconRsc = R.drawable.ic_subcategory_tools, colorCategory = R.color.categ_color_house_and_home_tools),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_GARDEN, SubcategoryName.SUBCATEGORY_GARDEN.label, iconRsc = R.drawable.ic_subcategory_garden, colorCategory = R.color.categ_color_house_and_home_garden),
            Category(CategoryName.CATEGORY_HOUSE_AND_HOME, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_house_and_home, colorCategory = R.color.categ_color_house_and_home_others),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_WATER, SubcategoryName.SUBCATEGORY_WATER.label, iconRsc = R.drawable.ic_subcategory_water, colorCategory = R.color.categ_color_services_water),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_LIGHT_GAS_AND_ENERGY, SubcategoryName.SUBCATEGORY_LIGHT_GAS_AND_ENERGY.label, iconRsc = R.drawable.ic_subcategory_energy, colorCategory = R.color.categ_color_services_light_gas_and_energy),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_INTERNET, SubcategoryName.SUBCATEGORY_INTERNET.label, iconRsc = R.drawable.ic_subcategory_internet, colorCategory = R.color.categ_color_services_internet),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_services, colorCategory = R.color.categ_color_services_others),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_STREAMING, SubcategoryName.SUBCATEGORY_STREAMING.label, iconRsc = R.drawable.ic_subcategory_streaming, colorCategory = R.color.categ_color_services_streaming),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_MUNICIPAL_TAX, SubcategoryName.SUBCATEGORY_MUNICIPAL_TAX.label, iconRsc = R.drawable.ic_subcategory_municipal, colorCategory = R.color.categ_color_services_municipal_tax),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_EXPENSES, SubcategoryName.SUBCATEGORY_EXPENSES.label, iconRsc = R.drawable.ic_subcategory_expenses, colorCategory = R.color.categ_color_services_expenses),
            Category(CategoryName.CATEGORY_SERVICES, SubcategoryName.SUBCATEGORY_MOBILE_TELEPHONY, SubcategoryName.SUBCATEGORY_MOBILE_TELEPHONY.label, iconRsc = R.drawable.ic_subcategory_telephony, colorCategory = R.color.categ_color_services_mobile_telephony),
            Category(CategoryName.CATEGORY_TECHNOLOGY, SubcategoryName.SUBCATEGORY_SOFTWARE_AND_GAMES, SubcategoryName.SUBCATEGORY_SOFTWARE_AND_GAMES.label, iconRsc = R.drawable.ic_subcategory_software_games, colorCategory = R.color.categ_color_technology_software),
            Category(CategoryName.CATEGORY_TECHNOLOGY, SubcategoryName.SUBCATEGORY_MAINTENANCE, SubcategoryName.SUBCATEGORY_MAINTENANCE.label, iconRsc = R.drawable.ic_category_technology, colorCategory = R.color.categ_color_technology_software),
            Category(CategoryName.CATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_MAIL_AND_SHIPMENTS, SubcategoryName.SUBCATEGORY_MAIL_AND_SHIPMENTS.label, iconRsc = R.drawable.ic_subcategory_mail_and_shipments, colorCategory = R.color.categ_color_others_mail_and_shipments),
            Category(CategoryName.CATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_LOTERY_CASINO, SubcategoryName.SUBCATEGORY_LOTERY_CASINO.label, iconRsc = R.drawable.ic_subcategory_lottery_casino, colorCategory = R.color.categ_color_others_lotery_casino),
            Category(CategoryName.CATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_MISSING, SubcategoryName.SUBCATEGORY_MISSING.label, iconRsc = R.drawable.ic_other_generic, colorCategory = R.color.categ_color_other),
            // TODO color: falta categ_color_others_administrative; se reusa categ_color_other como placeholder
            Category(CategoryName.CATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_ADMINISTRATIVE, SubcategoryName.SUBCATEGORY_ADMINISTRATIVE.label, iconRsc = R.drawable.ic_subcategory_administrative, colorCategory = R.color.categ_color_other),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_ASSESMENT, SubcategoryName.SUBCATEGORY_ASSESMENT.label, iconRsc = R.drawable.ic_subcategory_financial_assesment, colorCategory = R.color.categ_color_financial_expenses_assesment),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_CHARGES, SubcategoryName.SUBCATEGORY_CHARGES.label, iconRsc = R.drawable.ic_subcategory_charges, colorCategory = R.color.categ_color_financial_expenses_charges),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_FINES, SubcategoryName.SUBCATEGORY_FINES.label, iconRsc = R.drawable.ic_subcategory_fines, colorCategory = R.color.categ_color_financial_expenses_fines),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_INSURANCES, SubcategoryName.SUBCATEGORY_INSURANCES.label, iconRsc = R.drawable.ic_subcategory_financial_insurance, colorCategory = R.color.categ_color_financial_expenses_insurances),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_LOAN_INTERESTS, SubcategoryName.SUBCATEGORY_LOAN_INTERESTS.label, iconRsc = R.drawable.ic_subcategory_loan_interest, colorCategory = R.color.categ_color_financial_expenses_loan_interests),
            // TODO color: falta categ_color_financial_expenses_loan; se reusa categ_color_investment_and_finances como placeholder
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_LOAN, SubcategoryName.SUBCATEGORY_LOAN.label, iconRsc = R.drawable.ic_subcategory_loan, colorCategory = R.color.categ_color_investment_and_finances),
            Category(CategoryName.CATEGORY_FINANCIAL_EXPENSES, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_investment_and_finances, colorCategory = R.color.categ_color_investment_and_finances),
            Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_COFFEE_RESTAURANT, SubcategoryName.SUBCATEGORY_COFFEE_RESTAURANT.label, iconRsc = R.drawable.ic_subcategory_coffee_restaurants, colorCategory = R.color.categ_color_food_and_drinks_cofee_restaurant),
            Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_MARKET, SubcategoryName.SUBCATEGORY_MARKET.label, iconRsc = R.drawable.ic_subcategory_food_markets, colorCategory = R.color.categ_color_food_and_drinks_market),
            Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_FAST_FOOD, SubcategoryName.SUBCATEGORY_FAST_FOOD.label, iconRsc = R.drawable.ic_subcategory_fast_food, colorCategory = R.color.categ_color_fast_food),
            Category(CategoryName.CATEGORY_FOOD_AND_DRINKS, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_food, colorCategory = R.color.categ_color_food),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_PENSIONS, SubcategoryName.SUBCATEGORY_PENSIONS.label, iconRsc = R.drawable.ic_subcategory_pension, colorCategory = R.color.categ_color_income_pensions, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_CHECKS_COUPONS, SubcategoryName.SUBCATEGORY_CHECKS_COUPONS.label, iconRsc = R.drawable.ic_subcategory_check_coupons, colorCategory = R.color.categ_color_income_checks_coupons, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_SCHOLARSHIPS, SubcategoryName.SUBCATEGORY_SCHOLARSHIPS.label, iconRsc = R.drawable.ic_subcategory_scholarship, colorCategory = R.color.categ_color_income_scholarships, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_RENTALS, SubcategoryName.SUBCATEGORY_RENTALS.label, iconRsc = R.drawable.ic_subcategory_house_rental, colorCategory = R.color.categ_color_income_rentals, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_income, colorCategory = R.color.categ_color_income_others, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_PRODUCTS_SALE, SubcategoryName.SUBCATEGORY_PRODUCTS_SALE.label, iconRsc = R.drawable.ic_subcategory_products_sale, colorCategory = R.color.categ_color_income_products_sale, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_SALARY, SubcategoryName.SUBCATEGORY_SALARY.label, iconRsc = R.drawable.ic_subcategory_salary, colorCategory = R.color.categ_color_salary, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_SOCIAL_BENEFITS, SubcategoryName.SUBCATEGORY_SOCIAL_BENEFITS.label, iconRsc = R.drawable.ic_subcategory_social_benefit, colorCategory = R.color.categ_color_income_social_benefits, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_BONUS, SubcategoryName.SUBCATEGORY_BONUS.label, iconRsc = R.drawable.ic_subcategory_bonus, colorCategory = R.color.categ_color_income_bonus, isIncome = true),
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_REFUND_SHARED_EXPENSES, SubcategoryName.SUBCATEGORY_REFUND_SHARED_EXPENSES.label, iconRsc = R.drawable.ic_category_income, colorCategory = R.color.categ_color_income_others, isIncome = true),
            // TODO color: falta categ_color_income_exchange; se reusa categ_color_income_others como placeholder
            Category(CategoryName.CATEGORY_INCOME, SubcategoryName.SUBCATEGORY_EXCHANGE, SubcategoryName.SUBCATEGORY_EXCHANGE.label, iconRsc = R.drawable.ic_subcategory_exchange, colorCategory = R.color.categ_color_income_others, isIncome = true),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_TRADITIONAL, SubcategoryName.SUBCATEGORY_TRADITIONAL.label, iconRsc = R.drawable.ic_subcategory_tradit_investment, colorCategory = R.color.categ_color_investments_traditional),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_CRYPTOCURRENCY, SubcategoryName.SUBCATEGORY_CRYPTOCURRENCY.label, iconRsc = R.drawable.ic_subcategory_cryptocurrency, colorCategory = R.color.categ_color_investments_cryptocurrency),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_REAL_STATE, SubcategoryName.SUBCATEGORY_REAL_STATE.label, iconRsc = R.drawable.ic_subcategory_real_state, colorCategory = R.color.categ_color_investments_real_state),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_FOREIGN_CURRENCY, SubcategoryName.SUBCATEGORY_FOREIGN_CURRENCY.label, iconRsc = R.drawable.ic_subcategory_foreign_currency, colorCategory = R.color.categ_color_investments_foreign_currency),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_MOVABLE_ASSETS, SubcategoryName.SUBCATEGORY_MOVABLE_ASSETS.label, iconRsc = R.drawable.ic_subcategory_movable_assets, colorCategory = R.color.categ_color_investments_movable_assets),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_investment_and_finances, colorCategory = R.color.categ_color_investments_others),
            Category(CategoryName.CATEGORY_INVESTMENTS, SubcategoryName.SUBCATEGORY_HARD_ASSETS, SubcategoryName.SUBCATEGORY_HARD_ASSETS.label, iconRsc = R.drawable.ic_subcategory_hard_assets, colorCategory = R.color.categ_color_investments_hard_assets),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_BOOKS, SubcategoryName.SUBCATEGORY_BOOKS.label, iconRsc = R.drawable.ic_subcategory_books, colorCategory = R.color.categ_color_leisure_books),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_CONCERTS, SubcategoryName.SUBCATEGORY_CONCERTS.label, iconRsc = R.drawable.ic_subcategory_concerts, colorCategory = R.color.categ_color_concerts),
            // TODO color: falta categ_color_leisure_events; se reusa categ_color_leisure_others como placeholder
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_EVENTS, SubcategoryName.SUBCATEGORY_EVENTS.label, iconRsc = R.drawable.ic_subcategory_events, colorCategory = R.color.categ_color_leisure_others),
            // TODO color: falta categ_color_leisure_hotels; se reusa categ_color_leisure_others como placeholder. SUBCATEGORY_HOTELS es compartida con CATEGORY_TRAVEL (icono distinto por parent).
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_HOTELS, SubcategoryName.SUBCATEGORY_HOTELS.label, iconRsc = R.drawable.ic_subcategory_leisure_hotel, colorCategory = R.color.categ_color_leisure_others),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_CINEMA_AND_THEATER, SubcategoryName.SUBCATEGORY_CINEMA_AND_THEATER.label, iconRsc = R.drawable.ic_subcategory_cinema_theater, colorCategory = R.color.categ_color_leisure_cinema_and_theater),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_leisure, colorCategory = R.color.categ_color_leisure_others),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_CHARITY, SubcategoryName.SUBCATEGORY_CHARITY.label, iconRsc = R.drawable.ic_subcategory_charity, colorCategory = R.color.categ_color_leisure_charity),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_MUSEAMS_AND_CULTURE, SubcategoryName.SUBCATEGORY_MUSEAMS_AND_CULTURE.label, iconRsc = R.drawable.ic_subcategory_culture, colorCategory = R.color.categ_color_leisure_museams_and_culture),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_ALCOHOL_TOBACCO, SubcategoryName.SUBCATEGORY_ALCOHOL_TOBACCO.label, iconRsc = R.drawable.ic_subcategory_alcohol_tobacco, colorCategory = R.color.categ_color_leisure_alcohol_tobacco),
            Category(CategoryName.CATEGORY_LEISURE, SubcategoryName.SUBCATEGORY_PAINTING_DRAWING_AND_PHOTOGRAPHY, SubcategoryName.SUBCATEGORY_PAINTING_DRAWING_AND_PHOTOGRAPHY.label, iconRsc = R.drawable.ic_subcategory_painting_drawing_and_photography, colorCategory = R.color.categ_color_hobbies),
            // TODO color: falta un categ_color_education_and_development propio; se reusa categ_color_leisure_education_and_development como placeholder
            Category(CategoryName.CATEGORY_EDUCATION_AND_DEVELOPMENT, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_education, colorCategory = R.color.categ_color_leisure_education_and_development),
            // TODO color: falta un categ_color_sports propio; se reusa categ_color_leisure_sport como placeholder
            Category(CategoryName.CATEGORY_SPORTS, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_sports, colorCategory = R.color.categ_color_leisure_sport),
            // TODO color CATEGORY_TRAVEL: falta un categ_color_travel_* por entrada (hoy todas reusan categ_color_leisure_travel_and_holidays). INSURANCE / FOOD / OTHERS usan ic_category_travel (icono del padre) y se comparten con otras categorías.
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_TRAVEL_TICKETS, SubcategoryName.SUBCATEGORY_TRAVEL_TICKETS.label, iconRsc = R.drawable.ic_subcategory_travel_tickets, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_HOTELS, SubcategoryName.SUBCATEGORY_HOTELS.label, iconRsc = R.drawable.ic_subcategory_travel_hotel, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_INSURANCE, SubcategoryName.SUBCATEGORY_INSURANCE.label, iconRsc = R.drawable.ic_category_travel, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_FOOD, SubcategoryName.SUBCATEGORY_FOOD.label, iconRsc = R.drawable.ic_category_food, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_EXCURSIONS, SubcategoryName.SUBCATEGORY_EXCURSIONS.label, iconRsc = R.drawable.ic_subcategory_excursions, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_TRAVEL, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_travel, colorCategory = R.color.categ_color_leisure_travel_and_holidays),
            Category(CategoryName.CATEGORY_DEBT, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_subcategory_debt, colorCategory = R.color.categ_color_debt_others),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_APPLIANCES, SubcategoryName.SUBCATEGORY_APPLIANCES.label, iconRsc = R.drawable.ic_subcategory_appliances, colorCategory = R.color.categ_color_shops_appliances),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_GIFTS, SubcategoryName.SUBCATEGORY_GIFTS.label, iconRsc = R.drawable.ic_subcategory_gifts, colorCategory = R.color.categ_color_shops_gifts),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_ACCESSORIES, SubcategoryName.SUBCATEGORY_ACCESSORIES.label, iconRsc = R.drawable.ic_subcategory_accesories, colorCategory = R.color.categ_color_shops_accessories),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_PERSONAL_CARE, SubcategoryName.SUBCATEGORY_PERSONAL_CARE.label, iconRsc = R.drawable.ic_subcategory_personal_care, colorCategory = R.color.categ_color_shops_personal_care),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_shops, colorCategory = R.color.categ_color_shops_others),
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_CLOTHES_AND_SHOES, SubcategoryName.SUBCATEGORY_CLOTHES_AND_SHOES.label, iconRsc = R.drawable.ic_subcategory_clothes_and_shoes, colorCategory = R.color.categ_color_clothes),
            // TODO color: falta categ_color_shops_drugstore; se reusa categ_color_shops_others como placeholder
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_DRUGSTORE, SubcategoryName.SUBCATEGORY_DRUGSTORE.label, iconRsc = R.drawable.ic_subcategory_drugstore, colorCategory = R.color.categ_color_shops_others),
            // TODO color: falta categ_color_shops_electronics; se reusa categ_color_shops_others como placeholder
            Category(CategoryName.CATEGORY_SHOPS, SubcategoryName.SUBCATEGORY_ELECTRONICS, SubcategoryName.SUBCATEGORY_ELECTRONICS.label, iconRsc = R.drawable.ic_subcategory_electronics, colorCategory = R.color.categ_color_shops_others),
            Category(CategoryName.CATEGORY_PETS, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_pets, colorCategory = R.color.categ_color_pets_others),
            Category(CategoryName.CATEGORY_PETS, SubcategoryName.SUBCATEGORY_VET, SubcategoryName.SUBCATEGORY_VET.label, iconRsc = R.drawable.ic_subcategory_vet, colorCategory = R.color.categ_color_pets_vet),
            Category(CategoryName.CATEGORY_PETS, SubcategoryName.SUBCATEGORY_FOOD, SubcategoryName.SUBCATEGORY_FOOD.label, iconRsc = R.drawable.ic_subcategory_pet_food, colorCategory = R.color.categ_color_pets_food),
            Category(CategoryName.CATEGORY_PETS, SubcategoryName.SUBCATEGORY_DOG_WALKER, SubcategoryName.SUBCATEGORY_DOG_WALKER.label, iconRsc = R.drawable.ic_subcategory_dog_walker, colorCategory = R.color.categ_color_pets_dog_walker),
            Category(CategoryName.CATEGORY_TRANSPORTATION, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_transportation, colorCategory = R.color.categ_color_transportation_others),
            Category(CategoryName.CATEGORY_TRANSPORTATION, SubcategoryName.SUBCATEGORY_PUBLIC, SubcategoryName.SUBCATEGORY_PUBLIC.label, iconRsc = R.drawable.ic_subcategory_public_transport, colorCategory = R.color.categ_color_transportation_public),
            Category(CategoryName.CATEGORY_TRANSPORTATION, SubcategoryName.SUBCATEGORY_TAXI, SubcategoryName.SUBCATEGORY_TAXI.label, iconRsc = R.drawable.ic_subcategory_taxi, colorCategory = R.color.categ_color_transportation_taxi),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_HEALTH_INSURANCE, SubcategoryName.SUBCATEGORY_HEALTH_INSURANCE.label, iconRsc = R.drawable.ic_subcategory_health_insurance, colorCategory = R.color.categ_color_health_health_insurance),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICAL_SUPPLIES, SubcategoryName.SUBCATEGORY_MEDICAL_SUPPLIES.label, iconRsc = R.drawable.ic_subcategory_medical_supplies, colorCategory = R.color.categ_color_health_medical_supplies),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_BEAUTY, SubcategoryName.SUBCATEGORY_BEAUTY.label, iconRsc = R.drawable.ic_subcategory_beauty, colorCategory = R.color.categ_color_health_beauty),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_STUDIES, SubcategoryName.SUBCATEGORY_STUDIES.label, iconRsc = R.drawable.ic_subcategory_particular_studies, colorCategory = R.color.categ_color_studies),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_MEDICINE, SubcategoryName.SUBCATEGORY_MEDICINE.label, iconRsc = R.drawable.ic_subcategory_medicine, colorCategory = R.color.categ_color_medicine),
            Category(CategoryName.CATEGORY_HEALTH, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_health, colorCategory = R.color.categ_color_health),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_FUEL, SubcategoryName.SUBCATEGORY_FUEL.label, iconRsc = R.drawable.ic_subcategory_fuel, colorCategory = R.color.categ_color_vehicle_fuel),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_INSURANCE, SubcategoryName.SUBCATEGORY_INSURANCE.label, iconRsc = R.drawable.ic_subcategory_vehicle_insurance, colorCategory = R.color.categ_color_vehicle_insurance),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_OTHERS, SubcategoryName.SUBCATEGORY_OTHERS.label, iconRsc = R.drawable.ic_category_vehicle, colorCategory = R.color.categ_color_vehicles),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_PARKING, SubcategoryName.SUBCATEGORY_PARKING.label, iconRsc = R.drawable.ic_subcategory_parking, colorCategory = R.color.categ_color_vehicle_parking),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_RENTAL, SubcategoryName.SUBCATEGORY_RENTAL.label, iconRsc = R.drawable.ic_subcategory_vehicle_rental, colorCategory = R.color.categ_color_vehicle_rental),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_MAINTENANCE, SubcategoryName.SUBCATEGORY_MAINTENANCE.label, iconRsc = R.drawable.ic_subcategory_vehicle_maintenance, colorCategory = R.color.categ_color_vehicles_maintenance),
            Category(CategoryName.CATEGORY_VEHICLE, SubcategoryName.SUBCATEGORY_TOLLS, SubcategoryName.SUBCATEGORY_TOLLS.label, iconRsc = R.drawable.ic_subcategory_tolls, colorCategory = R.color.categ_color_vehicle_tolls),
            Category(CategoryName.WITHOUT_CATEGORY, SubcategoryName.SUBCATEGORY_NONE, CategoryName.WITHOUT_CATEGORY.label)
        )

        /**
         * Looks up the complete [Category] (with icon/color) for a given category/subcategory
         * pair, given as their persisted/CSV string keys (e.g. "CATEGORY_VEHICLE", "SUBCATEGORY_FUEL").
         * Falls back to a bare [Category] with default icon/color if the pair isn't found in
         * [ALL_CATEGORIES] (e.g. stale data from a previous taxonomy).
         */
        fun fromCategoryAndSubcategory(category: String, subcategory: String): Category {
            val categoryName = CategoryName.fromRaw(category)
            val subcategoryName = SubcategoryName.fromRaw(subcategory)
            return ALL_CATEGORIES.find { it.categoryName == categoryName && it.subcategoryName == subcategoryName }
                ?: Category(categoryName, subcategoryName)
        }
    }
}
