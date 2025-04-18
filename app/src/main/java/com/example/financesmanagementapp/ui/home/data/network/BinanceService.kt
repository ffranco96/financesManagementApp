package com.example.financesmanagementapp.ui.home.data.network

import android.util.Log
import com.example.financesmanagementapp.ui.home.core.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class BinanceService { // Retrofit has full abstraction in this class. If we want to switch endpoints we have to modify here
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getCryptoByTicker(token: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofit.create(BinanceAPIClient::class.java).getCryptoByTicker(token)
                val originalDouble = response.body()?.btcCurrentPrice?.toDoubleOrNull()
                BigDecimal(originalDouble?:0.0).setScale(2, RoundingMode.HALF_UP).toDouble()
            }catch(e: Exception){
                Log.e("HomeViewModel", "Excepción en getCryptoPrice: ${e.message}")
                null
            }
        }
    }
}