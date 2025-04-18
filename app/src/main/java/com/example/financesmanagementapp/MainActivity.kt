package com.example.financesmanagementapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme
import com.example.financesmanagementapp.navigation.AppNavigation


class MainActivity : ComponentActivity() {
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
