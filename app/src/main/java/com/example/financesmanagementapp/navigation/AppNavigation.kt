package com.example.financesmanagementapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financesmanagementapp.ui.addrecordetail.ui.AddRecordDetailScreen
import com.example.financesmanagementapp.ui.addrecordetail.ui.AddRecordDetailViewModel
import com.example.financesmanagementapp.ui.addregisteramount.ui.AddRecordAmountScreen
import com.example.financesmanagementapp.ui.addregisteramount.ui.AddRecordAmountViewModel
import com.example.financesmanagementapp.ui.home.ui.HomeStartScreen
import com.example.financesmanagementapp.ui.home.ui.HomeViewModel
import com.example.financesmanagementapp.ui.graphs.ui.ChartsScreen
import com.example.financesmanagementapp.ui.graphs.ui.ChartsViewModel
import com.example.financesmanagementapp.ui.login.ui.LoginScreen
import com.example.financesmanagementapp.ui.login.ui.LoginViewModel


/**
 * Composable element that will orchestrate navigation.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context: Context = LocalContext.current

    val startDestinationState = produceState(initialValue = AppScreens.LoginScreen.route) {
        val prefs = context.getSharedPreferences("financesMgmtAppPrefs", Context.MODE_PRIVATE)
        value = if (prefs.getBoolean("isLoggedIn", false)) {
            AppScreens.HomeStartScreen.route
        } else {
            AppScreens.LoginScreen.route
        }
    }

    NavHost(navController = navController, startDestination = startDestinationState.value) {
        composable(route = AppScreens.LoginScreen.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable(route = AppScreens.HomeStartScreen.route) {
            val homeViewModel: HomeViewModel =  hiltViewModel()
            HomeStartScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }
        composable(
            route = AppScreens.AddRecordAmountScreen.route,
        ) {
            val addRecordAmountViewModel: AddRecordAmountViewModel = hiltViewModel()
            AddRecordAmountScreen(
                navController,
                it.arguments?.getString("text"),
                addRecordAmountViewModel
            )
        }
        composable(route = AppScreens.AddRecordDetailScreen.route){
            val addRecordDetailViewModel: AddRecordDetailViewModel = hiltViewModel()
            AddRecordDetailScreen(
                navController = navController,
                addRecordDetailViewModel
            )
        }
        composable(route = AppScreens.ChartsScreen.route) {
            val chartsViewModel: ChartsViewModel = hiltViewModel()
            ChartsScreen(navController = navController, viewModel = chartsViewModel)
        }
    }
}
