package com.example.financesmanagementapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.financesmanagementapp.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStartScreen(navControler : NavController){
    Scaffold( topBar = {
        TopAppBar(title = {Text("Inicio")})
    }) {
        val a = it // To avoid error
        BodyContent(navControler)
    }
}

@Composable
fun BodyContent(navControler : NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Monto 0 ARS", style = MaterialTheme.typography.labelLarge)
        Button(onClick =
            {
                navControler.navigate(route = AppScreens.AddRegisterAmountScreen.route + "/Mi parametro") // Donde se displayara??
                Log.d("franco", "Button")
            }
        ){
            Text("+")
        }
    }
}