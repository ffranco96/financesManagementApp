package com.example.financesmanagementapp.ui.home.core
import com.example.financesmanagementapp.ui.home.data.network.BinanceAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: BinanceAPIService by lazy {
        Retrofit.Builder()
            .baseUrl("https://data-api.binance.vision/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BinanceAPIService::class.java)
    }
}