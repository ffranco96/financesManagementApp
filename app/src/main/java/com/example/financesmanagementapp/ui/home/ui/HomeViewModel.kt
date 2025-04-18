package com.example.financesmanagementapp.ui.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BtcValueState(val valueDouble: Double = 0.0)

class HomeViewModel : ViewModel() {
    private val _currentBtcValue = MutableStateFlow(BtcValueState())
    val currentBtcValue: StateFlow<BtcValueState> = _currentBtcValue
    val getBtcPriceUseCase = GetCryptoPriceUseCase()

    fun getCryptoPrice(ticker: String) {

        Log.d("franco", "ticker: $ticker")
        viewModelScope.launch {
            val btcPrice = getBtcPriceUseCase(ticker)
            Log.d("franco", "btc price nuevo: $btcPrice")
            btcPrice?.let {
                _currentBtcValue.value = BtcValueState(valueDouble = btcPrice)
            }
        }
    }
}