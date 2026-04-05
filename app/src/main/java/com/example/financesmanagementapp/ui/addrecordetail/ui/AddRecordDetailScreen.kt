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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.Record
import com.example.financesmanagementapp.ui.addrecordetail.model.Category

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


    val recordStateFlow = navController.previousBackStackEntry?.savedStateHandle?.getStateFlow<Record?>(
        "record", null
    )
    recordStateFlow?.let{
        Log.d("franco","Valor actual del Record desde RecordDetailScreen: ${recordStateFlow.value}")
    }

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
            FloatingActionButton(
                onClick = {
                    // In this situation, the user accepted the record creation and wants to save it
                    val uncompleteRecord: Record? = recordStateFlow?.value
                    val record = uncompleteRecord?.copy(
                        description = detailText,
                        category = categoryList.find { it.categoryName == selectedCategory } ?: Category()
                    )
                    viewModel.saveRecord(record)
                    navController.navigate(AppScreens.HomeStartScreen.route){
                        popUpTo(AppScreens.HomeStartScreen.route){
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.padding(16.dp),
                content = {Icon(Icons.Default.Check, contentDescription = "Aceptar")}
            )
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
            innerPadding = innerPadding
        )
    }
}

/**
 * Main content of the AddRecordDetailScreen.
 */
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
    innerPadding: PaddingValues
){
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
