package com.example.financesmanagementapp.api

import com.example.financesmanagementapp.model.database.BinanceApiBTCPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface BinanceAPIService {
    @GET("avgPrice")
    suspend fun getCryptoByTicker(@Query("symbol") token:String):Response<BinanceApiBTCPriceResponse>
}