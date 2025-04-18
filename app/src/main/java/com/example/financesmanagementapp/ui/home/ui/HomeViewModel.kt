package com.example.financesmanagementapp.ui.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.ui.home.core.RetrofitHelper
import com.example.financesmanagementapp.ui.home.data.network.BinanceAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

data class BtcValueState(val valueDouble: Double = 0.0)

class HomeViewModel : ViewModel() {
    private val binanceService = BinanceAPIService()
    private val _currentBtcValue = MutableStateFlow(BtcValueState())
    val currentBtcValue: StateFlow<BtcValueState> = _currentBtcValue

    fun getCryptoPrice(ticker: String) {
        Log.d("franco", "ticker: $ticker")
        viewModelScope.launch {
            val btcPrice = binanceService.getCryptoByTicker(ticker)
            Log.d("franco", "btc price nuevo: ${BigDecimal(btcPrice?:0.0).setScale(2, RoundingMode.HALF_UP).toDouble()}")
            btcPrice?.let {
                _currentBtcValue.value =
                    BtcValueState(
                        valueDouble = BigDecimal(btcPrice).setScale(
                            2,
                            RoundingMode.HALF_UP
                        ).toDouble()
                    )
            }
        }
    }
}