package com.example.financesmanagementapp.ui.graphs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * Composable screen that displays a bar chart of category spending.
 *
 * Shows an empty-state message ("No hay datos cargados") when [ChartsUiState.isEmpty] is true.
 * Provides a back button in the top bar that navigates to the previous screen via
 * [NavController.popBackStack].
 *
 * @param navController Controller used for navigation (back press).
 * @param viewModel ViewModel supplying the [ChartsUiState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    navController: NavController,
    viewModel: ChartsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráficos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isEmpty) {
                Text(
                    text = "No hay datos cargados",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Column {
                    uiState.categorySpendings.forEach { spending ->
                        Text(text = spending.categoryName)
                        Text(text = spending.totalAmount.toString())
                    }
                }
            }
        }
    }
}
