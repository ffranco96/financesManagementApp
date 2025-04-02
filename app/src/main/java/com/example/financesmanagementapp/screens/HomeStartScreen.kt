package com.example.financesmanagementapp.screens

import android.util.Log
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
import androidx.navigation.NavController
import com.example.financesmanagementapp.navigation.AppScreens

@Composable
fun HomeStartScreen(navControler : NavController){
    Scaffold() {
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
                /*TODO*/
                navControler.navigate(route = AppScreens.AddRegisterAmountScreen.route)
                Log.d("franco", "Button")
            }
        ){
            Text("+")
        }
    }
}