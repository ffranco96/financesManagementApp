package com.example.financesmanagementapp.ui.addrecordetail.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financesmanagementapp.domain.model.Category
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.navigation.AppScreens

/**
 * Screen that allows adding more details to a financial record, such as description and category.
 *
 * @param navController Controller for navigation between screens.
 * @param viewModel ViewModel that manages the state and logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDetailScreen(
    navController: NavController,
    viewModel: AddRecordDetailViewModel
){
    val detailText by viewModel.detail.collectAsState()
    val expandedCategoryMenu by viewModel.expandedCategoryMenu.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryList by viewModel.categories.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showDatePicker by viewModel.showDatePicker.collectAsState()
    val lastSelectedDateMillis by viewModel.lastSelectedDateMillis.collectAsState()


    val record by (navController.previousBackStackEntry?.savedStateHandle?.getStateFlow<Record?>("record", null)
        ?.collectAsState() ?: remember { mutableStateOf<Record?>(null) })

    Log.d("franco","Valor actual del Record desde RecordDetailScreen: $record")

    val isAmountZero = record?.amount == 0.0

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = lastSelectedDateMillis
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        })
                },
                title = {Text("Detalle")}
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                if (isAmountZero) {
                    Text(
                        text = "No se puede agregar\nun registro con monto 0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(8.dp))
                }
                FloatingActionButton(
                    onClick = {
                        if (!isAmountZero) {
                            val completeRecord = record?.copy(
                                description = detailText,
                                category = categoryList.find { it.categoryName == selectedCategory } ?: Category(),
                                date = selectedDate
                            )
                            viewModel.saveRecord(completeRecord)
                            navController.navigate(AppScreens.HomeStartScreen.route){
                                popUpTo(AppScreens.HomeStartScreen.route){
                                    inclusive = true
                                }
                            }
                        }
                    },
                    containerColor = if (isAmountZero) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isAmountZero) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Aceptar")
                }
            }
        }
    ) { innerPadding ->
        BodyContent(
            valueDetail = detailText,
            onDetailChange = { newValue ->
                viewModel.onDetailChange(newValue)
            },
            expanded = expandedCategoryMenu,
            onDropdownClick = {viewModel.onDropdownMenuClick()},
            onDismissRequest = {viewModel.onDismissRequest()},
            selectedCategory = selectedCategory,
            onCategorySelected = {newValue -> viewModel.onCategorySelected(newValue)},
            categoryList = categoryList,
            selectedDate = selectedDate,
            showDatePicker = showDatePicker,
            onDateClick = { viewModel.setShowDatePicker(true) },
            onDateSelected = { viewModel.onDateSelected(it) },
            onDatePickerDismiss = { viewModel.setShowDatePicker(false) },
            innerPadding = innerPadding,
            datePickerState = datePickerState,
        )
    }
}

/**
 * Main content of the AddRecordDetailScreen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(
    valueDetail : String,
    onDetailChange: (String) -> Unit,
    expanded: Boolean,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    categoryList: List<Category>,
    selectedDate: String,
    showDatePicker: Boolean,
    onDateClick: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    onDatePickerDismiss: () -> Unit,
    innerPadding: PaddingValues,
    datePickerState: DatePickerState,
){
    if (showDatePicker) {
        // Widget internal state

        DatePickerDialog(
            onDismissRequest = onDatePickerDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerDismiss) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese detalle", style = MaterialTheme.typography.titleLarge)
        TextField(
            value = valueDetail,
            onValueChange = onDetailChange,
            placeholder = {Text("" )},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = false,
            modifier = Modifier.height(80.dp).fillMaxWidth()
        )
        Spacer(Modifier.height(40.dp))

        Row(
            modifier = Modifier.align(Alignment.Start).clickable(onClick = onDropdownClick).fillMaxWidth().height(50.dp).padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val displayText = if (selectedCategory.isEmpty()) {
                "Seleccione categoría"
            } else {
                selectedCategory
            }

            Text(
                text = displayText,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .height(50.dp).padding(5.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category.categoryName) },
                        onClick = { onCategorySelected(category.categoryName) }
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Desplegar menu de categorias",
                modifier = Modifier.width(24.dp))
        }

        Spacer(Modifier.height(20.dp))

        // Date Selector
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onDateClick() }
                .fillMaxWidth()
                .height(50.dp)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val displayDate = if (selectedDate.length > 10) selectedDate.substring(0, 10) else selectedDate
            Text(
                text = "Fecha: $displayDate",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(5.dp)
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier.width(24.dp)
            )
        }

        Spacer(Modifier.weight(1f))


    }
}

/*@Preview(showBackground = true)
@Composable
fun AddRecordDetailScreenWithPreview(){
    val navController = rememberNavController()
    AddRecordDetailScreen(
        navController,
        AddRecordDetailContent()
    )
}*/
