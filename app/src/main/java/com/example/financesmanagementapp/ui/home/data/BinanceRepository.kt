package com.example.financesmanagementapp.ui.home.data

import com.example.financesmanagementapp.ui.home.data.network.BinanceService
import javax.inject.Inject

class BinanceRepository @Inject constructor(
    private val service : BinanceService
) { // This class will handle access to db or internet
    suspend fun getCryptoPrice(ticker: String): Double? {
        return service.getCryptoByTicker(ticker)
    }
}