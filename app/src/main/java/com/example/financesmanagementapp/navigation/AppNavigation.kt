package com.example.financesmanagementapp.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.financesmanagementapp.ui.home.data.model.RegisterEntity
import com.example.financesmanagementapp.ui.addregisteramount.ui.AddRegisterAmountScreen
import com.example.financesmanagementapp.ui.home.ui.HomeStartScreen
import com.example.financesmanagementapp.ui.login.ui.LoginScreen
import com.example.financesmanagementapp.ui.login.ui.LoginViewModel
import com.example.financesmanagementapp.ui.home.ui.HomeViewModel


// Composable element that will orchestrate navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    val context: Context = LocalContext.current

    val startDestinationState = produceState(initialValue = AppScreens.LoginScreen.route) {
        val prefs = context.getSharedPreferences("financesMgmtAppPrefs", Context.MODE_PRIVATE)
        value = if (prefs.getBoolean("isLoggedIn", false)) {
            AppScreens.HomeStartScreen.route
        } else {
            AppScreens.LoginScreen.route
        }
    }

    Log.d("franco", "startDestinationState: $startDestinationState")

    NavHost(navController = navController, startDestination = startDestinationState.value) {
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable(route = AppScreens.HomeStartScreen.route) {
            HomeStartScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }
        composable(
            route = AppScreens.AddRegisterAmountScreen.route + "/{text}",
            arguments = listOf(navArgument(name = "text") { type = NavType.StringType })
        ) {
            AddRegisterAmountScreen(navController, it.arguments?.getString("text"))
        }
    }
}
