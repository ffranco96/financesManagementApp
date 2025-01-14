package com.example.financesmanagementapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddRegisterAmountScreen(/*navController: NavController*/){
    Scaffold() {
        val a = it // Para evitar error
//        SecondBodyContent(navController)
    }
}

@Composable
fun SecondBodyContent(/*navController: NavController*/){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Monto 0 ARS", style = MaterialTheme.typography.labelLarge)
        Button(onClick = {/*TODO*/}){
            Text("+")
        }
    }
}