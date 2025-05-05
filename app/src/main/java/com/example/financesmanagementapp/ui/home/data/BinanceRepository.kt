package com.example.financesmanagementapp.ui.home.data

import com.example.financesmanagementapp.ui.home.data.model.BtcPriceProvider
import com.example.financesmanagementapp.ui.home.data.network.BinanceService
import javax.inject.Inject

class BinanceRepository @Inject constructor(
    private val service : BinanceService, //api??
    private val btcPriceProvider : BtcPriceProvider
) { // This class will handle access to db or internet
    suspend fun getCryptoPrice(ticker: String): Double {
        val response = service.getCryptoByTicker(ticker) ?:0.0
        btcPriceProvider.btcPrice = response
        return response
    }
}