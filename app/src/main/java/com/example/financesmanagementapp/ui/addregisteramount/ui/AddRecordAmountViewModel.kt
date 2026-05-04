package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.FiatCurrency
import com.example.financesmanagementapp.domain.usecase.GetFiatCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddRecordAmountViewModel @Inject constructor(
    private val getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase,
): ViewModel() {
    private val _amountInCents = MutableStateFlow(0L)
    val amountText: StateFlow<String> = _amountInCents.map { cents -> // When using map, it's converted to a cold Flow
        formatCentsToAmountText(cents)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0.00")

    private val _checkedSwitch = MutableStateFlow(false)
    val checkedSwitch: StateFlow<Boolean> = _checkedSwitch

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

    fun onCheckedSwitchChange(newValue: Boolean){
        _checkedSwitch.value = newValue
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