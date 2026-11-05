package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.usecase.GetCategoriesUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.UpdateRecordUseCase
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * ViewModel corresponding to the AddRecordDetailScreen.
 */
@HiltViewModel
class AddRecordDetailViewModel @Inject constructor(
    private val saveRecordUseCase: SaveRecordUseCase,
    private val updateRecordUseCase: UpdateRecordUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
): ViewModel() {
    private val _detail = MutableStateFlow("")
    val detail: StateFlow<String> = _detail

    private val _expandedCategoryMenu = MutableStateFlow(false)
    val expandedCategoryMenu: StateFlow<Boolean> = _expandedCategoryMenu

    private val _selectedCategory = MutableStateFlow(Category())
    val selectedCategory: StateFlow<Category> = _selectedCategory

    private val _selectedDate = MutableStateFlow(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    val selectedDate: StateFlow<String> = _selectedDate

    private val _lastSelectedDateMillis = MutableStateFlow<Long?>(null)
    val lastSelectedDateMillis: StateFlow<Long?> = _lastSelectedDateMillis

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    val categories: StateFlow<List<Category>> = getCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Preloads the form with an existing record so it can be edited.
     * Only meaningful for records already persisted (id != 0).
     */
    fun initializeFromRecord(record: Record) {
        _detail.value = record.description
        _selectedCategory.value = record.category
        if (record.date.isNotBlank()) {
            _selectedDate.value = record.date
            _lastSelectedDateMillis.value = parseDateToMillis(record.date)
        }
    }

    fun onDetailChange(newValue: String){
        _detail.value = newValue
    }

    fun onDropdownMenuClick(){
        _expandedCategoryMenu.value = !expandedCategoryMenu.value
    }

    fun onDismissRequest() {
        _expandedCategoryMenu.value = false
    }

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
        onDismissRequest()
    }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            _lastSelectedDateMillis.value = it
            val dateTime = LocalDateTime.of(
                Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate(),
                LocalTime.now()
            )
            _selectedDate.value = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
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
                if (it.id == 0) {
                    saveRecordUseCase(it)
                } else {
                    updateRecordUseCase(it)
                }
            }
        }
    }

    /**
     * Parses a record date (ISO date-time or legacy ISO date) into epoch millis at UTC,
     * matching the timezone used when [onDateSelected] converts the picker value back.
     */
    private fun parseDateToMillis(date: String): Long? = try {
        LocalDate.parse(date.take(10))
            .atStartOfDay(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
    } catch (_: DateTimeParseException) {
        null
    }
}
