package com.playsnyc.realistix.ui.screens.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.repositories.AuthRepository
import com.playsnyc.realistix.repositories.FireStoreRepository
import com.playsnyc.realistix.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.BackPressHandler
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import org.koin.androidx.compose.koinViewModel


@Composable fun SignUpScreen(
    navHostController: NavHostController,
    viewModel: SignUpScreenViewModel = koinViewModel(),
)
{

    val coroutineScope= rememberCoroutineScope()
    val uiState by viewModel.currentScreen.collectAsState()


    BackPressHandler{
        if(uiState==UiScreens.SIGNUP_SCREEN1)
            navHostController.navigateUp()
        else
            viewModel.changeScreen(getPrevScreen(uiState))
    }
    if (uiState==UiScreens.SIGNUP_SCREEN1)
    {
        SignUpScreen1(viewModel)
        return
    }
    if (uiState==UiScreens.SIGNUP_SCREEN2)
    {
        AnimatedVisibility(visible = true) {
            SignUpScreen2(viewModel)
        }

        return
    }
    if (uiState==UiScreens.SIGNUP_SCREEN3)
    {
        AnimatedVisibility(visible = true) {
            SignUpScreen3(viewModel)
        }
        return
    }
    if (uiState==UiScreens.SIGNUP_SCREEN4)
    {
        SignUpScreen4(viewModel)
        return
    }
    if (uiState==UiScreens.DASHBOARD)
    {
//        navHostController.navigate()

        navHostController.navigate(Screen.HowToUseScreen.route){
            popUpTo(navHostController.graph.startDestinationId)
            launchSingleTop = true
        }
        return
    }

//    if (uiState==SignUpScreens.SCREEN3)
//    {
//        SignUpScreen1(viewModel)
//        return
//    }


}

fun getPrevScreen(uiState: UiScreens): UiScreens
{
    return  when(uiState)
    {
        UiScreens.SIGNUP_SCREEN2 ->  UiScreens.SIGNUP_SCREEN1
        UiScreens.SIGNUP_SCREEN3 ->  UiScreens.SIGNUP_SCREEN2
        UiScreens.SIGNUP_SCREEN4 ->  UiScreens.SIGNUP_SCREEN3
        else-> {
            UiScreens.SIGNUP_SCREEN1
        }
    }
}

@Preview(showBackground = true) @Composable fun SignUpScreenPrev()
{
    RealisTixTheme {

        val sharedPref=SharedPref(LocalContext.current)
        SignUpScreen(

                NavHostController(LocalContext.current),
                viewModel = SignUpScreenViewModel(
                        FireStoreRepository(sharedPref),
                        AuthRepository(sharedPref)
                )
        )
    }

}