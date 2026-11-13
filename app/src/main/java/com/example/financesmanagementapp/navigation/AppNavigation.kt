package com.example.financesmanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
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
 * Resolves the initial route: Home when the `login_enabled` remote feature flag is off (login is
 * bypassed), Login when it is on. There is no persisted session yet, so when login is enabled the
 * screen is always shown on a cold start.
 */
internal fun resolveStartDestination(loginEnabled: Boolean): String =
    if (loginEnabled) {
        AppScreens.LoginScreen.route
    } else {
        AppScreens.HomeStartScreen.route
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

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            crashReporter.log("screen: ${destination.route}")
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    // isLoginEnabled() reads the cached Remote Config value synchronously, so the destination is
    // resolved before the NavHost is built. It is remembered so the read happens only once.
    val startDestination = remember {
        resolveStartDestination(featureFlagsRepository.isLoginEnabled())
    }

    NavHost(navController = navController, startDestination = startDestination) {
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
