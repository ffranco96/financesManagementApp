package com.example.financesmanagementapp.domain.model

/**
 * Enum class representing a crypto currency.
 *
 * @property code The unique code of the currency. Ticker of crypto currency
 * (Ej: BTC for Bitcoin).
 * @property description The description of the crypto currency.
 * IMPROVEMENT: Mover a un archivo de configuracion inicial
 */
enum class CryptoCurrency(
    val code: String,
    val description: String
) {
    BTC("BTC", "Bitcoin"),
    ETH("ETH", "Ethereum"),
    XRP("XRP", "Ripple");

    companion object {
        fun fromCode(code: String) = entries.find { it.code == code } ?: BTC
    }
}


