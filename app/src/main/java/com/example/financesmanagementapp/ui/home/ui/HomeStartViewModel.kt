package com.example.financesmanagementapp.ui.home.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.financesmanagementapp.data.local.ExportCsvUseCase
import com.example.financesmanagementapp.data.local.ParseCsvUseCase
import com.example.financesmanagementapp.data.local.ReadCsvUseCase
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.model.Record.Companion.DEFAULT_ACCOUNT_ID
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.home.data.worker.UpdateCryptoactivesWorker
import com.example.financesmanagementapp.ui.home.di.ServiceLocator
import com.example.financesmanagementapp.ui.home.domain.DeleteAllRecordsUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllCryptoPricesUseCase
import com.example.financesmanagementapp.ui.home.domain.GetAllRecordsFlowUseCase
import com.example.financesmanagementapp.ui.home.domain.GetCryptoPriceByTickerUseCase
import com.example.financesmanagementapp.ui.home.domain.GetTotalBalanceByAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 *
 * @property getBtcPriceUseCase Use case for getting the BTC price.
 * @property getAllCryptoPricesUseCase Use case for getting all crypto prices.
 * @property getAllRecordsFlowUseCase Use case for getting all records as a Flow.
 * @property deleteAllRecordsUseCase Use case for deleting all records.
 * @property readCsvUseCase Use case to select a local CSV file.
 * @property parseCsvUseCase Use case to parse a local CSV file.
 */
@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val getBtcPriceUseCase : GetCryptoPriceByTickerUseCase,
    private val getAllCryptoPricesUseCase: GetAllCryptoPricesUseCase,
    private val getAllRecordsFlowUseCase: GetAllRecordsFlowUseCase,
    private val deleteAllRecordsUseCase: DeleteAllRecordsUseCase,
    private val readCsvUseCase: ReadCsvUseCase,
    private val parseCsvUseCase: ParseCsvUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val exportCsvUseCase: ExportCsvUseCase,
    private val getTotalBalanceUseCase: GetTotalBalanceByAccountUseCase,
) : ViewModel(){
    private val _currentBtcValue = MutableStateFlow(0.0)
    val currentBtcValue: StateFlow<Double> = _currentBtcValue

    open val currentBalance: StateFlow<Double> = getTotalBalanceUseCase(DEFAULT_ACCOUNT_ID)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    private val _exportedCsvEvent = MutableSharedFlow<HomeUiEvent>()
    val exportedCsvEvent = _exportedCsvEvent.asSharedFlow()

    /**
     * A stateFlow, obtained from a flow that contains the updated list of records
     * present in the DB.
     */
    open val recordsList: StateFlow<List<Record>> = getAllRecordsFlowUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Se mantiene activo si hay alguien escuchando
            initialValue = emptyList() // Valor inicial mientras carga la DB
        )

    private val _btcPrice = MutableStateFlow("0.0")
    open val btcPrice: StateFlow<String> = _btcPrice

    init {
        if (ServiceLocator.getBtcPriceUseCase == null) {
            ServiceLocator.getBtcPriceUseCase = getBtcPriceUseCase
        }
    }

    fun clearRecordsList() {
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

    fun importCsv(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val readCsv = readCsvUseCase(uri)
            if (readCsv == null) {
                _exportedCsvEvent.emit(HomeUiEvent.ShowToast("Error al leer el archivo CSV"))
                return@launch
            }

            val recordList = parseCsvUseCase(readCsv)
            Log.d(TAG, "recordList leida del csv: $recordList")
            recordList.forEach { record ->
                saveRecordUseCase(record)
            }
            // TODO update balance
        }
    }

    /**
     * Function to export a list of records to a CSV file using ExportCsvUseCase.
     * Checks if there are records to export; if not, emits a UI event to show a Toast.
     */
    fun exportCsv() {
        viewModelScope.launch {
            val currentRecords = recordsList.value
            if (currentRecords.isEmpty()) {
                _exportedCsvEvent.emit(HomeUiEvent.ShowToast("No hay registros para exportar"))
                return@launch
            }

            val exportResult = exportCsvUseCase(currentRecords)
            if (exportResult) {
                _exportedCsvEvent.emit(HomeUiEvent.ShowToast("CSV exportado exitosamente"))
            } else {
                _exportedCsvEvent.emit(HomeUiEvent.ShowToast("Error al exportar CSV"))
            }
        }
    }

    companion object{
        const val TAG = "HomeViewModel"
    }
}