package com.example.financesmanagementapp.ui.home.data

import com.example.financesmanagementapp.ui.home.data.network.BinanceService

class BinanceRepository { // This class will handle access to db or internet
    private val service = BinanceService()
    suspend fun getCryptoPrice(ticker: String): Double? {
        return service.getCryptoByTicker(ticker)
    }
}