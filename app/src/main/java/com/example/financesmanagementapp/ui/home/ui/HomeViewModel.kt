package com.example.financesmanagementapp.ui.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _currentBtcValue = MutableStateFlow(0.0)
    val currentBtcValue: StateFlow<Double> = _currentBtcValue
    val getBtcPriceUseCase = GetCryptoPriceUseCase()

    private val _currentBalance = MutableStateFlow(0.0)
    val currentBalance: StateFlow<Double> = _currentBalance

    fun getCryptoPrice(ticker: String) {

        Log.d("franco", "ticker: $ticker")
        viewModelScope.launch {
            val btcPrice = getBtcPriceUseCase(ticker)
            Log.d("franco", "btc price nuevo: $btcPrice")
            btcPrice?.let {
                _currentBtcValue.value = btcPrice
            }
        }
    }
}