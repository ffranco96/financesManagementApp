package com.example.financesmanagementapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.financesmanagementapp.domain.crash.CrashReporter
import com.example.financesmanagementapp.domain.featureflags.FeatureFlagsRepository
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
 * Resolves the initial route: the login screen is skipped entirely (straight to Home) whenever
 * the `login_enabled` remote feature flag is off, since login is currently a no-op stub.
 */
internal fun resolveStartDestination(loginEnabled: Boolean, isLoggedIn: Boolean): String =
    if (!loginEnabled) {
        AppScreens.HomeStartScreen.route
    } else if (isLoggedIn) {
        AppScreens.HomeStartScreen.route
    } else {
        AppScreens.LoginScreen.route
    }

/**
 * Composable element that will orchestrate navigation.
 *
 * @param crashReporter used to log a breadcrumb on every destination change, so Crashlytics
 * reports show the screen the user was on. Registering it here covers every route in one place.
 * @param featureFlagsRepository used to decide whether the login screen should be part of the
 * initial navigation at all (see [resolveStartDestination]).
 */
@Composable
fun AppNavigation(crashReporter: CrashReporter, featureFlagsRepository: FeatureFlagsRepository) {
    val navController = rememberNavController()

    val context: Context = LocalContext.current

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            crashReporter.log("screen: ${destination.route}")
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    val startDestinationState = produceState(initialValue = AppScreens.LoginScreen.route) {
        val prefs = context.getSharedPreferences("financesMgmtAppPrefs", Context.MODE_PRIVATE)
        value = resolveStartDestination(
            loginEnabled = featureFlagsRepository.isLoginEnabled(),
            isLoggedIn = prefs.getBoolean("isLoggedIn", false)
        )
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
            arguments = listOf(navArgument(AppScreens.AddRecordAmountScreen.RECORD_ID_ARG) {
                type = NavType.IntType
                defaultValue = AppScreens.AddRecordAmountScreen.NO_RECORD_ID
            })
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
