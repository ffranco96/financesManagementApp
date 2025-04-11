package com.example.financesmanagementapp.model.database

import com.google.gson.annotations.SerializedName

data class BinanceApiBTCPriceResponse(
    @SerializedName("mins")var minutes:Int,
    @SerializedName("price")var btcCurrentPrice:String,
    @SerializedName("closeTime")var closeTime:Int
)

