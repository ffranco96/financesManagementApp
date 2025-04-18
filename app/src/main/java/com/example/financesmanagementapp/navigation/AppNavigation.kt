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

private val registersListInstance = mutableListOf(
    RegisterEntity(-1000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-2000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-3000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-4000.00,"Club de la milanesa", "", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-5000.00,"Club de la milanesa", "Lorem ipsum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-6000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-7000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-8000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-9000.00,"Club de la milanesa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-10000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-11000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-12000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS") ,
    RegisterEntity(-13000.00,"Club de la milanesa", "Pagamos a medias con mis amigos", "Restaurant y comida rapida", "2024-05-25", "ARS")
)

const val btcUsdtTicker = "BTCUSDT"

// Composable element that will orchestrate navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    val registersList = remember { mutableStateOf(registersListInstance) }
    val currentBalance = remember { mutableDoubleStateOf(0.0) }
    val currentBtcValue = homeViewModel.currentBtcValue.collectAsState()

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
                registersDetailList = registersList.value,
                currentBalance = currentBalance.doubleValue,
                currentBtcValueDouble = currentBtcValue.value.valueDouble, //TODO pasarle la instancia del viewmodel
                onBtcButtonClick = {
                    homeViewModel.getCryptoPrice(btcUsdtTicker)
                }
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
