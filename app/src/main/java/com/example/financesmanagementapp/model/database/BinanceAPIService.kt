package com.example.financesmanagementapp.model.database

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface BinanceAPIService {
    @GET
    fun getCryptoByTicker(@Url token:String):Response<BinanceApiBTCPriceResponse>
}