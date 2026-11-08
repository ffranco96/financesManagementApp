package com.example.financesmanagementapp.ui.addrecordetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.di.IoDispatcher
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.CategoryName
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.domain.usecase.GetCategoriesUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.addrecordetail.domain.UpdateRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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

    private var pendingWithoutCategoryRecord: Record? = null

    private val _pendingCategoryConfirmation = MutableStateFlow(false)

    /**
     * True while the "save without category" confirmation is being shown. The record
     * awaiting confirmation is held privately so the screen cannot save a different one.
     */
    val pendingCategoryConfirmation: StateFlow<Boolean> = _pendingCategoryConfirmation

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

    /**
     * Handles the save action, warning first when the record would be persisted without a category.
     *
     * @param record The record to persist, or null when there is nothing to save.
     * @return [SaveOutcome.SAVED] when it was persisted, [SaveOutcome.CONFIRMATION_REQUIRED] when
     * the user must confirm saving without a category first, or [SaveOutcome.IGNORED] when
     * [record] is null.
     */
    fun onSaveClicked(record: Record?): SaveOutcome {
        if (record == null) return SaveOutcome.IGNORED

        if (record.category.categoryName != CategoryName.WITHOUT_CATEGORY) {
            persist(record)
            return SaveOutcome.SAVED
        }

        pendingWithoutCategoryRecord = record
        _pendingCategoryConfirmation.value = true
        return SaveOutcome.CONFIRMATION_REQUIRED
    }

    /**
     * Persists the record that was waiting for the "without category" confirmation.
     */
    fun onSaveConfirmed() {
        val record = pendingWithoutCategoryRecord
        clearPendingConfirmation()
        record?.let { persist(it) }
    }

    /**
     * Discards the record that was waiting for the "without category" confirmation.
     */
    fun onSaveDismissed() {
        clearPendingConfirmation()
    }

    private fun clearPendingConfirmation() {
        pendingWithoutCategoryRecord = null
        _pendingCategoryConfirmation.value = false
    }

    private fun persist(record: Record) {
        viewModelScope.launch(ioDispatcher) {
            if (record.id == 0) {
                saveRecordUseCase(record)
            } else {
                updateRecordUseCase(record)
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

/**
 * Outcome of a save request, so the screen knows whether it must navigate away,
 * show the confirmation dialog, or do nothing.
 */
enum class SaveOutcome {
    /** The record was persisted and the screen can leave. */
    SAVED,

    /** A "without category" confirmation is pending; nothing was persisted yet. */
    CONFIRMATION_REQUIRED,

    /** There was no record to persist. */
    IGNORED,
}
