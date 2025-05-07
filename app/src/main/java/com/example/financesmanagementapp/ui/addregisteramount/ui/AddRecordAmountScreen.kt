package com.example.financesmanagementapp.ui.addregisteramount.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.financesmanagementapp.ui.Record

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRegisterAmountScreen(navController: NavController, text: String?) {
    var amountText by remember { mutableStateOf("") }

    Scaffold(topBar = {
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
                    Log.d("franco", "volviendo al home")
                    val myRecord = Record(amount = amount)
                    navController.previousBackStackEntry?.savedStateHandle?.set("record", myRecord)
                    navController.popBackStack()
                          },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Aceptar")
            }
        }
    ) {
        val a = it // To avoid error
        SecondBodyContent(
            valueAmountText = amountText,
            onValueAmountTextChange = { newValue ->
                if (newValue.all { it.isDigit() || it == '.' }) {
                    amountText = newValue
                }
            }
        )
    }
}

@Composable
fun SecondBodyContent(valueAmountText: String, onValueAmountTextChange: (String)-> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ingrese monto", style = MaterialTheme.typography.titleLarge)

        TextField(
            value = valueAmountText,
            onValueChange = onValueAmountTextChange,
            label = { Text("0.00", style = MaterialTheme.typography.bodyLarge) },
            placeholder = { Text("0.00", style = MaterialTheme.typography.bodyLarge) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddRegisterAmountScreenPreview() {
    // Crea un NavController simulado para la preview
    val navController = rememberNavController()

    // Llama a tu composable con valores de prueba
    AddRegisterAmountScreen(navController = navController, text = "Texto de ejemplo")
}