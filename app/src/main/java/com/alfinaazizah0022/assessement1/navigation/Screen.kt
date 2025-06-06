package com.alfinaazizah0022.assessement1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object AboutMe: Screen("aboutMeScreen")
}