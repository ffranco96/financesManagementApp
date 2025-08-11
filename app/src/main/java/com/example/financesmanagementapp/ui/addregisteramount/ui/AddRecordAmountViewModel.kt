package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddRecordAmountViewModel: ViewModel() {
    private val _amountText = MutableStateFlow("")
    val amountText: StateFlow<String> = _amountText

    private val _checkedSwitch = MutableStateFlow(false)
    val checkedSwitch: StateFlow<Boolean> = _checkedSwitch

    fun onAmountTextChange(newValue: String){
        if (newValue.all{it.isDigit() || it == '.'})
            _amountText.value = newValue
    }

    fun onCheckedSwitchChange(newValue: Boolean){
        _checkedSwitch.value = newValue
    }
}