package com.example.financesmanagementapp.ui.home.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class BinanceService @Inject constructor(private val api: BinanceAPIClient) { // Note: Retrofit has full abstraction in this class. If we want to switch endpoints we have to modify here
    // Note: In this case Retrofit cannot be edited to annotate with @Inject constructor. That's why
    // we use an api obtained from a NetworkModule to get Retrofit Singleton instance

    suspend fun getCryptoByTicker(token: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCryptoPriceByTicker(token)
                val originalDouble = response.body()?.btcCurrentPrice?.toDoubleOrNull()
                BigDecimal(originalDouble?:0.0).setScale(2, RoundingMode.HALF_UP).toDouble()
            }catch(e: Exception){
                Log.e("HomeViewModel", "Excepción en getCryptoPrice: ${e.message}")
                null
            }
        }
    }
}