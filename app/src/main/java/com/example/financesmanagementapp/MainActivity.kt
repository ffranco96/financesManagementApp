package com.example.financesmanagementapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.financesmanagementapp.model.database.BinanceAPIService
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme
import com.example.financesmanagementapp.navigation.AppNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    var _btc_price:Double? = 0.0 // Mover a view model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesManagementAppTheme {
                AppNavigation() // To know first screen (entry point)
            }
        }
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://data-api.binance.vision/api/v3/avgPrice")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getCryptoPrice(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(BinanceAPIService::class.java).getCryptoByTicker("?symbol=BTCUSDT")
            val btcPriceResponse = call.body()
            if(call.isSuccessful){
                // mostrar datos en home start
                _btc_price = btcPriceResponse?.btcCurrentPrice?.toDouble()
                Log.d("franco", "btc_price: ${_btc_price}")
            } else {
                // mostrar error
                showToastError()
            }
        }
    }

    private fun showToastError(){
        Toast.makeText(this, "Hubo un error", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppNavigation()
}