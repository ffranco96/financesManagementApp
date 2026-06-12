package com.example.financesmanagementapp.ui.graphs.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ChartsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysEmptyStateWhenNoData() {
        val mockViewModel = mockk<ChartsViewModel>(relaxed = true)
        every { mockViewModel.uiState } returns MutableStateFlow(
            ChartsUiState(isEmpty = true)
        )

        composeTestRule.setContent {
            ChartsScreen(
                navController = mockk(relaxed = true),
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("No hay datos cargados").assertIsDisplayed()
    }

    @Test
    fun displaysBarsForCategoriesWithTotals() {
        val mockViewModel = mockk<ChartsViewModel>(relaxed = true)
        val totals = listOf(
            CategoryTotal("Comida y alimentos", -150.0, R.color.categ_color_food),
            CategoryTotal("Salud", -50.0, R.color.categ_color_health)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(
            ChartsUiState(categoryTotals = totals, isEmpty = false)
        )

        composeTestRule.setContent {
            ChartsScreen(
                navController = mockk(relaxed = true),
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("Comida y alimentos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Salud").assertIsDisplayed()
    }

    @Test
    fun clickingBackNavigatesToHome() {
        val mockNavController = mockk<NavController>(relaxed = true)
        val mockViewModel = mockk<ChartsViewModel>(relaxed = true)
        every { mockViewModel.uiState } returns MutableStateFlow(
            ChartsUiState(isEmpty = true)
        )

        composeTestRule.setContent {
            ChartsScreen(
                navController = mockNavController,
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithContentDescription("Atrás").performClick()

        verify { mockNavController.popBackStack() }
    }

    @Test
    fun displaysAmountAboveEachBar() {
        val mockViewModel = mockk<ChartsViewModel>(relaxed = true)
        val totals = listOf(
            CategoryTotal("Comida y alimentos", -150.0, R.color.categ_color_food)
        )
        every { mockViewModel.uiState } returns MutableStateFlow(
            ChartsUiState(categoryTotals = totals, isEmpty = false)
        )

        composeTestRule.setContent {
            ChartsScreen(
                navController = mockk(relaxed = true),
                viewModel = mockViewModel
            )
        }

        composeTestRule.onNodeWithText("-150.0").assertIsDisplayed()
    }
}
