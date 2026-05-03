package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.usecase.GetCategoriesUseCase
import com.example.financesmanagementapp.ui.home.ui.HomeViewModel.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * ViewModel corresponding to the AddRecordDetailScreen.
 */
@HiltViewModel
class AddRecordDetailViewModel @Inject constructor(
    private val saveRecordUseCase: SaveRecordUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
): ViewModel() {
    private val _detail = MutableStateFlow("")
    val detail: StateFlow<String> = _detail

    private val _expandedCategoryMenu = MutableStateFlow(false)
    val expandedCategoryMenu: StateFlow<Boolean> = _expandedCategoryMenu

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _selectedDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
    val selectedDate: StateFlow<String> = _selectedDate

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    val categories: StateFlow<List<Category>> = getCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    fun onDateSelected(millis: Long?) {
        millis?.let {
            val date = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
            _selectedDate.value = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
        setShowDatePicker(false)
    }

    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    fun saveRecord(record: Record?) {
        viewModelScope.launch(Dispatchers.IO) {
            record?.let{
                Log.d(TAG, "Record to save: $record")
                saveRecordUseCase(record)
            }
        }
    }
}
