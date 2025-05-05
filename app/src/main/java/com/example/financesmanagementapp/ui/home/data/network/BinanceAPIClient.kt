package com.example.financesmanagementapp.ui.home.data.network

import com.example.financesmanagementapp.ui.home.data.model.BinanceApiBTCPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceAPIClient {
    @GET("avgPrice")
    suspend fun getCryptoPriceByTicker(@Query("symbol") token:String):Response<BinanceApiBTCPriceResponse>
}