package com.lifecycle.weatherz.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object History : Screen("weather_history_screen")
}