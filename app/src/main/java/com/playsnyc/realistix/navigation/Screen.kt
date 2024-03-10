package com.playsnyc.realistix.navigation

sealed class Screen(val route: String) {

    object StartScreen : Screen("start")
    object LoginScreen : Screen("login")
    object SignUpScreen : Screen("register")
    object HowToUseScreen : Screen("how_to_use")
    object DashBoardScreen : Screen("dashboard")


    fun args(vararg args: String): String {
        val argsString = args.joinToString("/")
        return "$route/$argsString"
    }
}