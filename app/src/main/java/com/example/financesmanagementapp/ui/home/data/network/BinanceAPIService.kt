package com.example.financesmanagementapp.ui.home.data.network

import android.util.Log
import com.example.financesmanagementapp.ui.home.core.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BinanceAPIService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getCryptoByTicker(token: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    retrofit.create(BinanceAPIClient::class.java).getCryptoByTicker(token)
                response.body()?.btcCurrentPrice?.toDoubleOrNull()
            }catch(e: Exception){
                Log.e("HomeViewModel", "Excepción en getCryptoPrice: ${e.message}")
                null
            }
        }
    }
}