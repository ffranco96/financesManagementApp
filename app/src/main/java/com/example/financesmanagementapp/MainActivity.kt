package com.example.financesmanagementapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.financesmanagementapp.api.BinanceAPIService
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme
import com.example.financesmanagementapp.navigation.AppNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    //var _btc_price:Double? = 0.0 // Mover a view model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesManagementAppTheme {
                AppNavigation()
            }
        }
    }

    private fun showToastError(){
        Toast.makeText(this, "Hubo un error", Toast.LENGTH_SHORT).show()
    }
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppNavigation()
}*/