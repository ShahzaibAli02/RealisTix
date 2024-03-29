package com.playsnyc.realistix.ui.screens.auth.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.playsnyc.realistix.R
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.model.errorMessage
import com.playsnyc.realistix.model.isError
import com.playsnyc.realistix.model.isLoading
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.composables.ErrorText
import com.playsnyc.realistix.ui.composables.RoundProgress
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.playsnyc.realistix.utils.MyPermissionHelper
import com.screen.mirroring.extensions.roundClickable
import com.screen.mirroring.extensions.toDp
import org.koin.androidx.compose.koinViewModel

//val context = LocalContext.current
//val permissionHelper = MyPermissionHelper().initComposable()
@Composable fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginScreenViewModel = koinViewModel(),
)
{

    val uiState by viewModel.uiState.collectAsState()
    val email by viewModel._email.collectAsState()
    val password by viewModel._password.collectAsState()

    if (uiState.uiScreen == UiScreens.DASHBOARD)
    {
        navController.navigate(Screen.DashBoardScreen.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
        return
    }

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        Text(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp),
                fontSize = 38.sp,
                text = stringResource(R.string.login),
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Bold
        )


        Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo RX"
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = "Email",
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = email,
                onValueChange = {
                    viewModel.update { viewModel._email.value = it }
                })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon",
                            tint = Color.Black
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            letterSpacing = 1.5.sp,
                            text = "Password",
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = password,
                onValueChange = {
                    viewModel.update { viewModel._password.value = it }
                })


        if (uiState.isError)
        {
            ErrorText(text = uiState.errorMessage)
        }
        Spacer(modifier = Modifier.height(30.dp))
        ElevatedButton(modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp),
                shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Black),
                onClick = {
                    viewModel.login()
                }) {
            if (uiState.isLoading)
                RoundProgress(modifier = Modifier.size(30.dp))
            else Text(
                    color = MyColors.current._FFFFFF,
                    fontSize = 20.sp,
                    text = "Log In",
                    fontFamily = MyFonts.poppins()
            )


        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
                modifier = Modifier.roundClickable {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                textAlign = TextAlign.Center,
                text = "Don't have an account? \n" + "Sign up here",
                fontSize = 15.sp,
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Normal
        )


    }
}


@Preview(showBackground = true) @Composable fun LoginScreenPrev()
{
    RealisTixTheme {
        LoginScreen(NavHostController(LocalContext.current))
    }

}
