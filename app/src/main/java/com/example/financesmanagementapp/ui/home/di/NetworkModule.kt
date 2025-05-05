package com.example.financesmanagementapp.ui.home.di

import com.example.financesmanagementapp.ui.home.data.network.BinanceAPIClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class) // Note: Application scope. Not related with Singleton classic meaning
object NetworkModule {
    // Provide Retrofit

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://data-api.binance.vision/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideBinanceAPIClient(retrofit: Retrofit):BinanceAPIClient{
        return retrofit.create(BinanceAPIClient::class.java)
    }

}