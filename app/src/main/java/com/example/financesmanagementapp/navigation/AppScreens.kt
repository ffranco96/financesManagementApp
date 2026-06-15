package com.example.financesmanagementapp.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object HomeStartScreen: AppScreens("home_start_screen")
    object AddRecordAmountScreen: AppScreens("add_record_amount_screen")
    object AddRecordDetailScreen: AppScreens("add_record_detail_screen")
    object ChartsScreen: AppScreens("charts_screen")
}