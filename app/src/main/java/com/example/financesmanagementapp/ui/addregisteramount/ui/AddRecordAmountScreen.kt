package com.example.financesmanagementapp.ui.addregisteramount.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financesmanagementapp.domain.model.Record
import com.example.financesmanagementapp.navigation.AppScreens

/**
 * Screen that allows the user to input the amount for a new financial record.
 * It also handles the type of operation (Income/Expense) and currency selection.
 *
 * @param navController Controller for navigation between screens.
 * @param text Optional text parameter (currently unused).
 * @param viewModel ViewModel that manages the state and logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordAmountScreen(
    navController: NavController,
    text: String?,
    viewModel: AddRecordAmountViewModel,
) {
    val amountText by viewModel.amountText.collectAsState()
    val expandedCurrencyMenu by viewModel.expandedCurrencyMenu.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val editingRecord by viewModel.editingRecord.collectAsState()
    val currencyList by viewModel.currencyList.collectAsState()

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
                title = { Text(if (viewModel.isEditing) "Editar registro" else "Agregar registro") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val base = editingRecord
                    // Avoid continuing before the record to edit has loaded, and never
                    // replace an existing record with a fresh one when editing.
                    if (!viewModel.isEditing || base != null) {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        val myRecord = base?.copy(amount = amount, currency = selectedCurrency)
                            ?: Record(amount = amount, currency = selectedCurrency)
                        navController.currentBackStackEntry?.savedStateHandle?.set("record", myRecord)
                        navController.navigate(AppScreens.AddRecordDetailScreen.route)
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Siguiente pantalla")
            }
        }
    ) { innerPadding ->
        BodyContent(
            valueAmountText = amountText,
            onAmountTextChange = { newValue ->
                viewModel.onAmountTextChange(newValue)
            },
            expanded = expandedCurrencyMenu,
            onDropdownClick = {viewModel.onDropDownClick()},
            onDismissRequest = {viewModel.onDismissRequest()},
            selectedCurrency = selectedCurrency,
            onCurrencySelected = {  newValue ->
                viewModel.onCurrencySelected(newValue) },
            currencyList = currencyList,
            innerPadding = innerPadding
        )
    }
}

/**
 * Main content of the AddRecordAmountScreen.
 */
@Composable
fun BodyContent(
    valueAmountText: String,
    onAmountTextChange: (String) -> String,
    expanded: Boolean,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: List<String>,
    innerPadding: PaddingValues
) {
    // The amount field is fully controlled by the ViewModel: every change is cleaned there and
    // the clean text is written back here. `neverEqualPolicy` forces a recomposition even when
    // the clean text is the same as before, so rejected characters (decimal separators) never
    // stay visible in the field.
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = valueAmountText,
                selection = TextRange(valueAmountText.length)
            ),
            neverEqualPolicy()
        )
    }

    // Keep the field in sync when the amount changes from outside of it (for example, when an
    // existing record finishes loading while editing).
    LaunchedEffect(valueAmountText) {
        if (valueAmountText != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = valueAmountText,
                selection = TextRange(valueAmountText.length)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese monto", fontSize = 42.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val cleanText = onAmountTextChange(newValue.text)
                textFieldValue = TextFieldValue(
                    text = cleanText,
                    selection = TextRange(cleanText.length)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontSize = 55.sp
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent, // Opcional: oculta la línea inferior al enfocar
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)){

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val displayCurrency = selectedCurrency.ifEmpty { "ARS" }

                Text(
                    text = displayCurrency,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(onClick = onDropdownClick)
                        .fillMaxHeight()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = onDismissRequest
                ) {
                    currencyList.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(text = currency) },
                            onClick = { onCurrencySelected(currency) }
                        )
                    }
                }
            }
        }
    }
}
