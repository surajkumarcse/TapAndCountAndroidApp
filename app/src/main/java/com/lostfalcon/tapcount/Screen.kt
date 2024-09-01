package com.lostfalcon.tapcount

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object AppInfoScreen : Screen("app_info_screen")
}