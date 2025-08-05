package com.example.financesmanagementapp.ui.addrecordetail.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddRecordDetailViewModel: ViewModel() {
    private val _detail = MutableStateFlow("")
    val detail: StateFlow<String> = _detail

    fun onDetailChange(newValue: String){
        newValue.let{
            _detail.value = newValue
        }
    }
}
