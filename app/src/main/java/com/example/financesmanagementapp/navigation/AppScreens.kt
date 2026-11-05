package com.example.financesmanagementapp.navigation

sealed class AppScreens(val route: String) {
    object LoginScreen: AppScreens("login_screen")
    object HomeStartScreen: AppScreens("home_start_screen")
    object AddRecordAmountScreen : AppScreens("add_record_amount_screen?recordId={recordId}") {
        const val RECORD_ID_ARG = "recordId"
        const val NO_RECORD_ID = -1
        fun createRoute(recordId: Int = NO_RECORD_ID): String =
            "add_record_amount_screen?$RECORD_ID_ARG=$recordId"
    }
    object AddRecordDetailScreen: AppScreens("add_record_detail_screen")
    object ChartsScreen: AppScreens("charts_screen")
}