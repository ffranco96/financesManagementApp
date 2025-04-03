package com.example.financesmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme
import com.example.financesmanagementapp.navigation.AppNavigation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesManagementAppTheme {
                AppNavigation() // To know first screen (entry point)
                //RegistersList(registersListInstance) TODO move to HomeStartScreen
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppNavigation()
}