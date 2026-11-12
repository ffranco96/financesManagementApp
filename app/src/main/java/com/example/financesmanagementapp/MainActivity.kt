package com.example.financesmanagementapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.financesmanagementapp.domain.crash.CrashReporter
import com.example.financesmanagementapp.domain.featureflags.FeatureFlagsRepository
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme
import com.example.financesmanagementapp.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var crashReporter: CrashReporter

    @Inject
    lateinit var featureFlagsRepository: FeatureFlagsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesManagementAppTheme {
                AppNavigation(crashReporter, featureFlagsRepository)
            }
        }
    }

    private fun showToastError(){
        Toast.makeText(this, "Hubo un error", Toast.LENGTH_SHORT).show()
    }
}
