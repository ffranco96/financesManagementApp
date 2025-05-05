package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import com.example.financesmanagementapp.utils.Constants
import javax.inject.Inject


class GetAllCryptoPricesUseCase @Inject constructor(
    private val binanceRepository: BinanceRepository
) {
    suspend operator fun invoke(): List<Double>{
        val listOfPrices = mutableListOf<Double>()
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.BTC_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.ETH_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.DOGE_USDT_TICKER))
        listOfPrices.add(binanceRepository.getCryptoPrice(Constants.XRP_USDT_TICKER))
        return listOfPrices
    }
}