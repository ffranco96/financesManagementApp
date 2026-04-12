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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financesmanagementapp.navigation.AppScreens
import com.example.financesmanagementapp.ui.Record

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
    val checkedSwitch by viewModel.checkedSwitch.collectAsState()
    val expandedCurrencyMenu by viewModel.expandedCurrencyMenu.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

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
                title = { Text("Agregar registro") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    val myRecord = Record(amount = amount, isIncome = checkedSwitch, currency = selectedCurrency)
                    navController.currentBackStackEntry?.savedStateHandle?.set("record", myRecord)
                    navController.navigate(AppScreens.AddRecordDetailScreen.route)
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
            onCheckedSwitchChange = { newValue ->
                viewModel.onCheckedSwitchChange(newValue) },
            checkedSwitch = checkedSwitch,
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
    onAmountTextChange: (String) -> Unit,
    onCheckedSwitchChange: (Boolean) -> Unit,
    checkedSwitch: Boolean,
    expanded: Boolean,
    onDropdownClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: List<String>,
    innerPadding: PaddingValues
) {
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
            value = valueAmountText,
            onValueChange = onAmountTextChange,
            placeholder = {
                Text(
                    "0.00",
                    fontSize = 55.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontSize = 55.sp
            ),
        )

        Spacer(Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)){
            Text("Gasto", fontSize = 22.sp)
            Switch(
                checked = checkedSwitch,
                onCheckedChange = onCheckedSwitchChange,
                modifier = Modifier.weight(1f),
                thumbContent = if(checkedSwitch) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.Green
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft, // TODO poner signo de resta
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.Red
                        )
                    }
                }
            )
            Text("Ingreso", fontSize = 22.sp)

            Spacer(Modifier.weight(1f))

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

/*@Preview(showBackground = true)
@Composable
fun AddRecordAmountScreenPreview() {
    val navController = rememberNavController()
    val viewModel = AddRecordAmountViewModel()

    AddRecordAmountScreen(navController = navController, text = "Texto de ejemplo", viewModel = viewModel )
}*/
