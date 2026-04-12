package com.example.financesmanagementapp.domain.model

/**
 * Data class representing a currency.
 *
 * @property abbrev The unique code of the currency. Can be the official ISO
 * code for fiat currencies (Ej: ARS for argentinian pesos), or the ticker for crypto currencies
 * (Ej: BTC for Bitcoin).
 * @property description The description of the currency.
 */
data class Currency(
    val abbrev: String,
    val description: String
)
