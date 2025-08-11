package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddRecordAmountViewModel: ViewModel() {
    private val _amountText = MutableStateFlow("")
    val amountText: StateFlow<String> = _amountText

    private val _checkedSwitch = MutableStateFlow(false)
    val checkedSwitch: StateFlow<Boolean> = _checkedSwitch

    private val _expandedCurrencyMenu = MutableStateFlow(false)
    val expandedCurrencyMenu: StateFlow<Boolean> = _expandedCurrencyMenu

    private val _selectedCurrency = MutableStateFlow("ARS") // TODO va a venir de primer valor de las currencies por DI
    val selectedCurrency: StateFlow<String> = _selectedCurrency

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