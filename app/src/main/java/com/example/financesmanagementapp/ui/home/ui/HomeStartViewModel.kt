package com.example.financesmanagementapp.ui.home.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.financesmanagementapp.data.local.entities.RecordEntity
import com.example.financesmanagementapp.ui.home.data.worker.UpdateCryptoactivesWorker
import com.example.financesmanagementapp.ui.home.di.ServiceLocator
import com.example.financesmanagementapp.ui.home.domain.DeleteAllRecordsUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllCryptoPricesUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllRecordsFlowUseCase
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceByTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBtcPriceUseCase : GetCryptoPriceByTickerUseCase,
    private val getAllCryptoPricesUseCase: GetAllCryptoPricesUseCase,
    private val getAllRecordsFlowUseCase: GetAllRecordsFlowUseCase,
    private val deleteAllRecordsUseCase: DeleteAllRecordsUseCase,
) : ViewModel(){
    private val _currentBtcValue = MutableStateFlow(0.0)
    val currentBtcValue: StateFlow<Double> = _currentBtcValue

    private val _currentBalance = MutableStateFlow(0.0)
    val currentBalance: StateFlow<Double> = _currentBalance

    val recordsList: StateFlow<List<RecordEntity>> = getAllRecordsFlowUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Se mantiene activo si hay alguien escuchando
            initialValue = emptyList() // Valor inicial mientras carga la DB
        )

    private val _btcPrice = MutableStateFlow("0.0")
    val btcPrice: StateFlow<String> = _btcPrice

    init {
        if (ServiceLocator.getBtcPriceUseCase == null) {
            ServiceLocator.getBtcPriceUseCase = getBtcPriceUseCase
        }
    }

    fun clearRegistersList() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllRecordsUseCase()
        }
    }

    fun setupWorkers(context: Context){
        val btcPriceWorkRequest = PeriodicWorkRequestBuilder<UpdateCryptoactivesWorker>(1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("btc_price_worker",
            ExistingPeriodicWorkPolicy.KEEP,btcPriceWorkRequest)
    }

    fun updateBtcPrice(newPrice: String){
        _btcPrice.value = newPrice
    }

    fun getCryptoPrice(ticker: String) {

        Log.d(TAG, "ticker: $ticker")
        viewModelScope.launch {
            //val listOfPrices = getAllCryptoPricesUseCase()
            //Log.d("franco", "listOfPrices: $listOfPrices")
            val btcPrice = getBtcPriceUseCase(ticker)
            Log.d(TAG, "Btc new price: $btcPrice")
            btcPrice.let {
                _btcPrice.value = btcPrice.toString()
            }
        }
    }

    companion object{
        const val TAG = "HomeViewModel"
    }
}