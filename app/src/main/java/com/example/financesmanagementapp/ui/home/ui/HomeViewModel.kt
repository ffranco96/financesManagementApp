package com.example.financesmanagementapp.ui.home.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.financesmanagementapp.ui.home.data.model.RegisterEntity
import com.example.financesmanagementapp.ui.home.data.worker.UpdateCryptoactivesWorker
import com.example.financesmanagementapp.ui.home.di.ServiceLocator
import com.example.financesmanagementapp.ui.home.domain.GetAllCryptoPricesUseCase
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceByTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val registersListExample = mutableListOf(
    RegisterEntity(-1000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-2000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-3000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-4000.00,"Club de la milanesa", "", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-5000.00,"Club de la milanesa", "Lorem ipsum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-6000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-7000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-8000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-9000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-10000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-11000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-12000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-13000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS")
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBtcPriceUseCase : GetCryptoPriceByTickerUseCase,
    private val getAllCryptoPricesUseCase: GetAllCryptoPricesUseCase
) : ViewModel(){
    private val _currentBtcValue = MutableStateFlow(0.0)
    val currentBtcValue: StateFlow<Double> = _currentBtcValue

    private val _currentBalance = MutableStateFlow(0.0)
    val currentBalance: StateFlow<Double> = _currentBalance

    private val _registersList = MutableStateFlow(registersListExample)
    val registersList: StateFlow<MutableList<RegisterEntity>> = _registersList

    init {
        if (ServiceLocator.getBtcPriceUseCase == null) {
            ServiceLocator.getBtcPriceUseCase = getBtcPriceUseCase
        }
    }

    fun clearRegistersList() {
        Log.d("franco", "clearRegistersList")
        _registersList.value = mutableListOf()
    }

    fun setupWorkers(context: Context){
        val btcPriceWorkRequest = PeriodicWorkRequestBuilder<UpdateCryptoactivesWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("btc_price_worker",
            ExistingPeriodicWorkPolicy.KEEP,btcPriceWorkRequest)
    }

    fun getCryptoPrice(ticker: String) {

        Log.d("franco", "ticker: $ticker")
        viewModelScope.launch {
            //val listOfPrices = getAllCryptoPricesUseCase()
            //Log.d("franco", "listOfPrices: $listOfPrices")
            val btcPrice = getBtcPriceUseCase(ticker)
            Log.d("franco", "btc price nuevo: $btcPrice")
            btcPrice?.let {
                _currentBtcValue.value = btcPrice
            }
        }
    }
}