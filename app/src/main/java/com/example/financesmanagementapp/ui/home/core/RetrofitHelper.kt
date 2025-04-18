package com.example.financesmanagementapp.ui.home.core
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://data-api.binance.vision/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}