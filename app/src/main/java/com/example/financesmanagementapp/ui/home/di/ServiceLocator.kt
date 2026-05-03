package com.example.financesmanagementapp.ui.home.di

import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceByTickerUseCase

object ServiceLocator {
    var getBtcPriceUseCase: GetCryptoPriceByTickerUseCase? = null
}
