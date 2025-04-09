package com.alfinaazizah0022.assessement1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}