package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.usecase.GetFiatCurrenciesUseCase
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.addregisteramount.domain.GetRecordByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToLong

@HiltViewModel
class AddRecordAmountViewModel @Inject constructor(
    private val getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase,
    private val getRecordByIdUseCase: GetRecordByIdUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val recordId: Int =
        savedStateHandle.get<Int>(AppScreens.AddRecordAmountScreen.RECORD_ID_ARG)
            ?: AppScreens.AddRecordAmountScreen.NO_RECORD_ID

    /** True when the screen was opened to edit an existing record. Navigation intent */
    val isEditing: Boolean = recordId != AppScreens.AddRecordAmountScreen.NO_RECORD_ID

    private val _editingRecord = MutableStateFlow<Record?>(null)

    /** The record being edited. Can be null while creating a new one or before it loads. */
    val editingRecord: StateFlow<Record?> = _editingRecord

    private val _amountInCents = MutableStateFlow(0L)
    val amountText: StateFlow<String> = _amountInCents.map { cents -> // When using map, it's converted to a cold Flow
        formatCentsToAmountText(cents)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0.00")

    private val _expandedCurrencyMenu = MutableStateFlow(false)
    val expandedCurrencyMenu: StateFlow<Boolean> = _expandedCurrencyMenu

    private val _selectedCurrency = MutableStateFlow(FiatCurrency.ARS.code) // TODO va a venir de primer valor de las currencies por DI
    val selectedCurrency: StateFlow<String> = _selectedCurrency

    var currencyList: StateFlow<List<String>> = getFiatCurrenciesUseCase()
        .map{ it.map{currency -> currency.code} }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        if (isEditing) {
            viewModelScope.launch {
                getRecordByIdUseCase(recordId)?.let { record ->
                    _editingRecord.value = record
                    // Amounts are persisted signed; this screen works in absolute cents.
                    _amountInCents.value = (abs(record.amount) * 100).roundToLong()
                    _selectedCurrency.value = record.currency.ifBlank { FiatCurrency.ARS.code }
                }
            }
        }
    }

    fun onAmountTextChange(newValue: String){
        val digits = newValue.filter { it.isDigit() }
        if(digits.isEmpty()){
            _amountInCents.value = 0L
            return
        }
        if(digits.length <= MAX_AMOUNT_TEXT_LENGTH){
            _amountInCents.value = digits.toLong()
        }
    }

    private fun formatCentsToAmountText(cents: Long): String {
        val doubleValue = cents / 100.0
        return "%.2f".format(Locale.US, doubleValue)
    }

    fun onDropDownClick(){
        _expandedCurrencyMenu.value = !expandedCurrencyMenu.value
    }

    fun onDismissRequest() {
        _expandedCurrencyMenu.value = false
    }

    fun onCurrencySelected(currency: String) {
        _selectedCurrency.value = currency
        onDismissRequest()
    }

    companion object{
        const val MAX_AMOUNT_TEXT_LENGTH = 12

    }
}