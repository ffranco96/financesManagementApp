package com.example.financesmanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.financesmanagementapp.screens.AddRegisterAmountScreen
import com.example.financesmanagementapp.screens.HomeStartScreen

// Composable element that will orchestrate navigation
@Composable
fun AppNavigation(){
    val navController = rememberNavController() // Manages navigation status among screens
    NavHost(navController = navController, startDestination = AppScreens.HomeStartScreen.route){
        composable(route = AppScreens.HomeStartScreen.route){
            HomeStartScreen(navController)
        }
        composable(route = AppScreens.AddRegisterAmountScreen.route + "/{text}",
            arguments = listOf(navArgument(name = "text"){
                type = NavType.StringType
            })
        ){
            AddRegisterAmountScreen(navController, it.arguments?.getString("text"))
        }
    }
}