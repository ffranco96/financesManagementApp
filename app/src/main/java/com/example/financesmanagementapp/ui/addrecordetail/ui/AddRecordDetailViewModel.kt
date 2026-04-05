package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.Record
import com.example.financesmanagementapp.ui.addrecordetail.domain.SaveRecordUseCase
import com.example.financesmanagementapp.ui.addrecordetail.model.Category
import com.example.financesmanagementapp.ui.home.ui.HomeViewModel.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel corresponding to the AddRecordDetailScreen.
 */
@HiltViewModel
class AddRecordDetailViewModel @Inject constructor(
    private val saveRecordUseCase: SaveRecordUseCase,
): ViewModel() {
    private val _detail = MutableStateFlow("")
    val detail: StateFlow<String> = _detail

    private val _expandedCategoryMenu = MutableStateFlow(false)
    val expandedCategoryMenu: StateFlow<Boolean> = _expandedCategoryMenu

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _categories = MutableStateFlow(listOf(
        Category("Comida y alimentos", R.drawable.ic_category_food, R.color.categ_color_food, ""),
        Category("Restaurant y comida rapida", R.drawable.ic_category_fast_food, R.color.categ_color_fast_food, ""),
        Category("Ropa", R.drawable.ic_category_clothes, R.color.categ_color_clothes, ""),
        Category("Vehiculos", R.drawable.ic_category_vehicle, R.color.categ_color_vehicles, ""),
        Category("Mantenimiento vehiculos", R.drawable.ic_category_vehicle_maintenance, R.color.categ_color_vehicles_maintenance, ""),
        Category("Recitales y eventos", R.drawable.ic_category_concerts, R.color.categ_color_concerts, ""),
        Category("Salud", R.drawable.ic_category_health, R.color.categ_color_health, ""),
        Category("Estudios particulares", R.drawable.ic_category_particular_studies, R.color.categ_color_studies, ""),
        Category("Medicamentos e insumos", R.drawable.ic_category_medicine, R.color.categ_color_medicine, ""),
        Category("Hobbies", R.drawable.ic_category_hobbies, R.color.categ_color_hobbies, ""),
        Category("Pintura, dibujo y fotografia", R.drawable.ic_category_painting_drawing_and_photography, R.color.categ_color_painting_drawing_photos, ""),
        Category("Inversiones y finanzas", R.drawable.ic_category_investment_and_finances, R.color.categ_color_investment_and_finances, ""),
        Category("Salario", R.drawable.ic_category_salary, R.color.categ_color_salary, ""),
        Category("Otros", R.drawable.ic_other_generic, R.color.categ_color_other, ""),
    )) // TODO inyectar desde configuracion
    val categories: StateFlow<List<Category>> = _categories

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

    fun saveRecord(record: Record?) {
        viewModelScope.launch(Dispatchers.IO) {
            record?.let{
                Log.d(TAG, "Record to save: $record")
                saveRecordUseCase(record)
            }
        }
    }
}
