package com.playsnyc.realistix.ui.screens.splash

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playsnyc.realistix.extensions.nonScaledSp
import androidx.navigation.compose.rememberNavController
import com.playsnyc.realistix.Constants
import com.playsnyc.realistix.Constants.CHANGE_CALL_INTENT_FILTER
import com.playsnyc.realistix.R
import com.playsnyc.realistix.utils.MyFonts
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.extensions.connectivityState
import com.playsnyc.realistix.data.model.ConnectionState
import com.playsnyc.realistix.navigation.NavigationView
import com.playsnyc.realistix.navigation.Screen

import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyBroadcastReceiver

class StartActivity : ComponentActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState) //        setTheme(R.style.SplashTheme)
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        setContent {
            RealisTixTheme { // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val connection by connectivityState()
                val isConnected = connection === ConnectionState.Available
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current
                    var isInCall by rememberSaveable { mutableStateOf(false) }
                    val broadcastReceiver = remember {
                        MyBroadcastReceiver() { intent ->
                            isInCall = intent!!.getBooleanExtra(
                                    Constants.EXTRA_IN_CALL,
                                    false
                            )
                        }
                    }

                    DisposableEffect(Unit) {
                        LocalBroadcastManager.getInstance(context).registerReceiver(
                                broadcastReceiver,
                                IntentFilter(CHANGE_CALL_INTENT_FILTER)
                        ) // LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(CHANGE_CALL_INTENT_FILTER).putExtra("request_status",true))
                        if(FirebaseAuth.getInstance().currentUser!=null)
                        {
                            navController.navigate(Screen.DashBoardScreen.route){
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        onDispose {
                            LocalBroadcastManager.getInstance(context)
                                .unregisterReceiver(broadcastReceiver)
                        }
                    }
                    Column(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = !isConnected) {
                            noInternet()
                        }
                        NavigationView(navController)
                    }

                }
            }
        }
    }


    @Composable private fun noInternet()
    {
        val context = LocalContext.current
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyPerColors._EC1707)
                    .clickable { //                    resumeMirroringActivity(context)
                    },
        ) {
            Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.weight(1f))
                Image(
                        modifier = Modifier
                            .padding(
                                    start = 5.dp,
                                    end = 10.dp
                            )
                            .size(10.dp),
                        colorFilter = ColorFilter.tint(Color.White),
                        painter = painterResource(id = R.drawable.baseline_wifi_off_24),
                        contentDescription = ""
                )
                Text(
                        fontFamily = MyFonts.montserrat,
                        modifier = Modifier.wrapContentHeight(),
                        text = "No Internet Connection!",
                        fontSize = 8.nonScaledSp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(color = Color.White),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }


    }


    override fun onDestroy()
    {
        super.onDestroy()
    }

    @Preview @Composable fun InCallPrev()
    {
        noInternet()
    }

}