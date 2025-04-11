package com.example.financesmanagementapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.api.BinanceAPIService
import com.example.financesmanagementapp.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class BtcValueState(val valueDouble: Double = 0.0)

class HomeViewModel : ViewModel() {

    private val _currentBtcValue = MutableStateFlow(BtcValueState())
    val currentBtcValue: StateFlow<BtcValueState> = _currentBtcValue

    fun getCryptoPrice(ticker: String) {
        Log.d("franco", "ticker: $ticker")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val call = RetrofitInstance.api.getCryptoByTicker(ticker)
                if (call.isSuccessful) {
                    val btcPrice = call.body()?.btcCurrentPrice?.toDoubleOrNull()
                    btcPrice?.let {
                        _currentBtcValue.value = BtcValueState(valueDouble = it)
                        //Log.d("HomeViewModel", "btc_price: $it")
                    }
                } else {
                    Log.e("HomeViewModel", "Error de respuesta en la API")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Excepción en getCryptoPrice: ${e.message}")
            }
        }
    }
}