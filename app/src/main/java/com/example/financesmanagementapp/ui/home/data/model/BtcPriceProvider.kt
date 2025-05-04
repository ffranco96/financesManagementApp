package com.example.financesmanagementapp.ui.home.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BtcPriceProvider @Inject constructor() {
    var btcPrice:Double = 0.0
}