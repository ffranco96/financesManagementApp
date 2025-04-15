package com.example.financesmanagementapp.navigation

sealed class AppScreens(val route: String) {
    object HomeStartScreen: AppScreens("home_start_screen")
    object AddRegisterAmountScreen: AppScreens("add_register_amount_screen")
    object LoginScreen: AppScreens("login_screen")
}