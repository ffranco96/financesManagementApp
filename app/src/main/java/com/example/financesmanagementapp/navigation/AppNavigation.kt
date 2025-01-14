package com.example.financesmanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.example.financesmanagementapp.screens.AddRegisterAmountScreen
import com.example.financesmanagementapp.screens.HomeStartScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeStartScreen.route){
        composable(route = AppScreens.HomeStartScreen.route){
            HomeStartScreen(navController)
        }
        composable(route = AppScreens.AddRegisterAmountScreen.route){
            AddRegisterAmountScreen(navController)
        }
    }
}