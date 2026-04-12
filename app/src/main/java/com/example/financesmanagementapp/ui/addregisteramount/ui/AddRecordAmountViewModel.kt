package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.usecase.GetFiatCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddRecordAmountViewModel @Inject constructor(
    private val getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase,
): ViewModel() {
    private val _amountText = MutableStateFlow("")
    val amountText: StateFlow<String> = _amountText

    private val _checkedSwitch = MutableStateFlow(false)
    val checkedSwitch: StateFlow<Boolean> = _checkedSwitch

    private val _expandedCurrencyMenu = MutableStateFlow(false)
    val expandedCurrencyMenu: StateFlow<Boolean> = _expandedCurrencyMenu

    private val _selectedCurrency = MutableStateFlow("ARS") // TODO va a venir de primer valor de las currencies por DI
    val selectedCurrency: StateFlow<String> = _selectedCurrency

    var currencyList: StateFlow<List<String>> = getFiatCurrenciesUseCase()
        .map{ it.map{currency -> currency.abbrev} }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAmountTextChange(newValue: String){
        if (newValue.all{it.isDigit() || it == '.'})
            _amountText.value = newValue
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
}