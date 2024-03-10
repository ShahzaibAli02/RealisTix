package com.playsnyc.realistix.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.playsnyc.realistix.ui.screens.auth.StartScreen
import com.playsnyc.realistix.ui.screens.auth.login.LoginScreen
import com.playsnyc.realistix.ui.screens.auth.signup.SignUpScreen
import com.playsnyc.realistix.ui.screens.dashboard.DashBoardScreen
import com.playsnyc.realistix.ui.screens.howtouse.HowToUseScreen


@Composable
fun NavigationView(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.StartScreen.route) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(navController)
        }
        composable(Screen.HowToUseScreen.route) {
            HowToUseScreen(navController)
        }
        composable(Screen.DashBoardScreen.route) {
            DashBoardScreen(navController)
        }

    }
}