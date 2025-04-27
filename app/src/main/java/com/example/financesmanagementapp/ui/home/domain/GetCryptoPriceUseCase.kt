package com.example.financesmanagementapp.ui.home.domain

import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import javax.inject.Inject

class GetCryptoPriceUseCase @Inject constructor() {
    private val repository = BinanceRepository()

    suspend operator fun invoke(ticker: String): Double? {
        return repository.getCryptoPrice(ticker)
    }
}