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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    val currencyList by viewModel.currencyList.collectAsState()

    AddRecordAmountContent(
        amountText = amountText,
        expandedCurrencyMenu = expandedCurrencyMenu,
        selectedCurrency = selectedCurrency,
        currencyList = currencyList,
        onAmountTextChanged = viewModel::onAmountTextChange,
        onDropDownClick = viewModel::onDropDownClick,
        onDismissRequest = viewModel::onDismissRequest,
        onCurrencySelected = viewModel::onCurrencySelected,
        onBackClick = { navController.popBackStack() },
        onNextClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("record", viewModel.buildRecord())
            navController.navigate(AppScreens.AddRecordDetailScreen.route)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordAmountContent(
    amountText: String,
    expandedCurrencyMenu: Boolean,
    selectedCurrency: String,
    currencyList: List<String>,
    onAmountTextChanged: (String) -> Unit,
    onDropDownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onCurrencySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        modifier = Modifier.clickable(onClick = onBackClick)
                    )
                },
                title = { Text("Agregar registro") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNextClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Siguiente pantalla")
            }
        }
    ) { innerPadding ->
        BodyContent(
            valueAmountText = amountText,
            onAmountTextChange = { newValue ->
                onAmountTextChanged(newValue)
            },
            expanded = expandedCurrencyMenu,
            onDropdownClick = onDropDownClick,
            onDismissRequest = onDismissRequest,
            selectedCurrency = selectedCurrency,
            onCurrencySelected = { newValue ->
                onCurrencySelected(newValue)
            },
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
    onAmountTextChange: (String) -> Unit,
    expanded: Boolean,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: List<String>,
    innerPadding: PaddingValues
) {
    // Synchronized with the view model valueAmountText
    var textFieldValue by remember(valueAmountText) {
        mutableStateOf(
            TextFieldValue(
                text = valueAmountText,
                selection = TextRange(valueAmountText.length)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Ingrese monto",
            fontSize = 42.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val forcedCursorToEnd = newValue.copy(selection = TextRange(newValue.text.length))
                textFieldValue = forcedCursorToEnd
                onAmountTextChange(forcedCursorToEnd.text)
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
