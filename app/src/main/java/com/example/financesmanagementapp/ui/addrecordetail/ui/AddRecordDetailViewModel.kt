package com.example.financesmanagementapp.ui.addrecordetail.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel corresponding to the AddRecordDetailScreen.
 */
class AddRecordDetailViewModel: ViewModel() {
    private val _detail = MutableStateFlow("")
    val detail: StateFlow<String> = _detail

    private val _expandedCategoryMenu = MutableStateFlow(false)
    val expandedCategoryMenu: StateFlow<Boolean> = _expandedCategoryMenu

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    fun onDetailChange(newValue: String){
        _detail.value = newValue
    }

    fun onDropdownMenuClick(){
        _expandedCategoryMenu.value = !expandedCategoryMenu.value
    }

    fun onDismissRequest() {
        _expandedCategoryMenu.value = false
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        onDismissRequest()
    }
}
