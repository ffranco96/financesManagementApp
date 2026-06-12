package com.example.financesmanagementapp.ui.graphs.ui

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme

/**
 * Composable screen that displays a bar chart of category total amount.
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
    ChartsScreenContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() }
    )
}

/**
 * Content-only version of the Charts screen, useful for previews and testing
 * since it has no dependency on [NavController] or [ChartsViewModel].
 *
 * @param uiState The current UI state to render.
 * @param onBackClick Callback invoked when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartsScreenContent(
    uiState: ChartsUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráficos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                    uiState.categoryTotals.forEach { total ->
                        Text(text = total.categoryName)
                        Text(text = total.totalAmount.toString())
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Empty state",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ChartsScreenEmptyPreview() {
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(isEmpty = true),
            onBackClick = {}
        )
    }
}

@Preview(
    name = "With data",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ChartsScreenDataPreview() {
    val totals = listOf(
        CategoryTotal("Comida y alimentos", -150.0, R.color.categ_color_food),
        CategoryTotal("Salud", -50.0, R.color.categ_color_health),
        CategoryTotal("Salario", 200.0, R.color.categ_color_salary)
    )
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(categoryTotals = totals, isEmpty = false),
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Empty state - dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChartsScreenEmptyDarkPreview() {
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(isEmpty = true),
            onBackClick = {}
        )
    }
}
