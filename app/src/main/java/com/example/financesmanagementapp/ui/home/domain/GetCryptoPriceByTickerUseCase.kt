package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import javax.inject.Inject

/**
 * Use case to retrieve the current price of a specific cryptocurrency by its ticker.
 *
 * @property repository Repository to fetch data from Binance API.
 */
class GetCryptoPriceByTickerUseCase @Inject constructor(
    private val repository : BinanceRepository
) {
    /**
     * Executes the use case to get the price of a ticker (e.g., BTCUSDT).
     *
     * @param ticker The symbol of the cryptocurrency pair.
     * @return The current price as a Double.
     */
    suspend operator fun invoke(ticker: String): Double {
        return repository.getCryptoPrice(ticker)
    }
}