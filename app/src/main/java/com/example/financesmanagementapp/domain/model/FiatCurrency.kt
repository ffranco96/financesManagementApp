package com.example.financesmanagementapp.domain.model

/**
 * Enum class representing a fiat currency.
 *
 * @property code The unique code of the currency. Official ISO code of the fiat currency
 * (Ej: ARS for argentinian pesos),.
 * @property description The description of the currency.
 * IMPROVEMENT: Mover a un archivo de configuracion inicial
 */
enum class FiatCurrency(
    val code: String,
    val description: String
) {
    ARS("ARS", "Pesos argentinos"),
    USD("USD", "Dólar estadounidense"),
    EUR("EUR", "Euros"),
    BRL("BRL", "Reales brasileros");

    companion object {
        fun fromCode(code: String) = entries.find { it.code == code } ?: ARS
    }
}


