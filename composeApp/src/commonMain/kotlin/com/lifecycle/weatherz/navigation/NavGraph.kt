package com.lifecycle.weatherz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lifecycle.weatherz.presentation.screen.history.WeatherHistoryScreen
import com.lifecycle.weatherz.presentation.screen.home.HomeScreen
import com.lifecycle.weatherz.util.showMessage

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                showMessage = {message ->
                    showMessage(message)
                }

            )
        }

        composable(route = Screen.History.route) {
            WeatherHistoryScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
