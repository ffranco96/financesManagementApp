package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import com.example.financesmanagementapp.utils.Constants
import javax.inject.Inject

/**
 * Use case to retrieve the prices of all supported cryptocurrencies.
 *
 * @property binanceRepository Repository to fetch data from Binance API.
 */
class GetAllCryptoPricesUseCase @Inject constructor(
    private val binanceRepository: BinanceRepository
) {
    /**
     * Executes the use case to get a list of prices for BTC, ETH, DOGE, and XRP.
     *
     * @return A list of prices in USDT for the predefined cryptocurrencies.
     */
    suspend operator fun invoke(): List<Double>{
        val listOfPrices = mutableListOf<Double>()
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.BTC_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.ETH_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.DOGE_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.XRP_USDT_TICKER))
        return listOfPrices
    }
}