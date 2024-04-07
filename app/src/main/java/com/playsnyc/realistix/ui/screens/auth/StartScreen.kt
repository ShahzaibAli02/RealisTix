package com.playsnyc.realistix.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.playsnyc.realistix.R
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.playsnyc.realistix.utils.MyPermissionHelper
import com.playsnyc.realistix.extensions.toDp
import com.playsnyc.realistix.extensions.toSp


@Composable fun StartScreen(
    navController: NavHostController,
)
{ //val startScreenState by startViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val permissionHelper = MyPermissionHelper().initComposable()
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
    ) {

        Image(
                alignment = Alignment.BottomCenter,
                modifier = Modifier
                    .size(
                            width = 300.dp,
                            height = 200.dp
                    )
                    .padding(0.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo RX"
        )

        Text(
                fontSize = com.intuit.sdp.R.dimen._20sdp.toSp(),
                text = stringResource(R.string.app_name),
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                text = "Bringing People\nTogether",
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Black),
                onClick = {
                    navController.navigate(Screen.LoginScreen.route)
                }) {
            Row(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                    Color.White,
                                    shape = CircleShape
                            )
                ) {
                    Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(com.intuit.sdp.R.dimen._10sdp.toDp()))
                Text(
                        color = Color.White,
                        fontSize = com.intuit.sdp.R.dimen._12sdp.toSp(),
                        text = "Log in with your account",
                        fontFamily = MyFonts.poppins(),
                        fontWeight = FontWeight.Bold
                )

            }

        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                onClick = {
                    navController.navigate(Screen.SignUpScreen.route)

                }) {
            Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = com.intuit.sdp.R.dimen._12sdp.toSp(),
                    text = "Sign up",
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}



@Preview(showBackground = true) @Composable fun StartScreenPrev()
{
    RealisTixTheme {
        StartScreen(NavHostController(LocalContext.current))
    }

}
