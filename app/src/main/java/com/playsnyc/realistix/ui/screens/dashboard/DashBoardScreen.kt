package com.playsnyc.realistix.ui.screens.dashboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.enums.BottomNavItems
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.createevent.CreateEventScreen
import com.playsnyc.realistix.ui.screens.createevent.CreateEventScreen2
import com.playsnyc.realistix.ui.screens.home.EventDetailScreen
import com.playsnyc.realistix.ui.screens.home.HomeScreen
import com.playsnyc.realistix.ui.screens.profile.ProfileScreen
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.screen.mirroring.extensions.roundClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


object HeaderText
{
    val headerText: MutableSharedFlow<String> = MutableSharedFlow(1)
    val messageHeader: MutableSharedFlow<HeaderMessage> = MutableSharedFlow(1)
}

@Composable fun DashBoardScreen(mainNavController: NavHostController)
{
    var backButtonVisibility by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    backButtonVisibility = BottomNavItems.entries.find { currentRoute == it.route } == null
    Scaffold(bottomBar = {
        BottomNavigationBar(navController)
    }) { innerPadding ->

        var headerText by rememberSaveable { mutableStateOf("") }
        var messageHeader by rememberSaveable { mutableStateOf<HeaderMessage?>(null) }
        headerText = getHeaderText(currentRoute)
        LaunchedEffect(Unit) {
            launch {
                HeaderText.messageHeader.collectLatest {
                    messageHeader = it
                    delay(1000)
                    messageHeader = null
                }
            }

        }
        Column(modifier = Modifier.padding(innerPadding)) {
            MessageHeader(messageHeader)
            Header(modifier = Modifier.fillMaxWidth(),
                    showBack = backButtonVisibility,
                    onBackPressed = {
                        navController.navigateUp()
                    })
            SubHeader(
                    modifier = Modifier,
                    headerText
            )
            BottomNavigationView(
                    modifier = Modifier
                        .background(MyColors.current._F3F3F3)
                        .fillMaxWidth()
                        .weight(1f),
                    navController = navController
            )
        }

    }
}


@Composable fun MessageHeader(message: HeaderMessage?)
{
    if (message == null) return
    val typography = MaterialTheme.typography
    Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(message.backGroundColor)
                .padding(10.dp),
            text = message.message,
            style = typography.titleMedium.copy(
                    color = message.textColor,
                    textAlign = TextAlign.Center
            ),
    )

}

@Preview @Composable fun HeaderPrev()
{

    val header = HeaderMessage("Test Message")
    SubHeader(text = "Event")
}

@Composable fun Header(modifier: Modifier = Modifier, showBack: Boolean, onBackPressed: () -> Unit)
{
    Column(modifier) {
        Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
        ) {
            if (showBack)
            {
                Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(40.dp)
                            .roundClickable {
                                onBackPressed()
                            },
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back Arrow"
                )
            }

            Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Divider()
    }
}

@Composable fun SubHeader(modifier: Modifier = Modifier, text: String)
{

    val typography = MaterialTheme.typography
    Column(modifier = modifier.width(IntrinsicSize.Max)) {
        Text(
                modifier = Modifier.padding(5.dp),
                text = text,
                style = typography.titleMedium,
        )
        Divider(
                modifier = Modifier.height(4.dp),
                color = MyPerColors.primary_color
        )
    }

}


@Preview(showBackground = true) @Composable fun DashBoardScreenPrev()
{
    RealisTixTheme {
        DashBoardScreen(NavHostController(LocalContext.current))
    }
}


fun getHeaderText(currentRoute: String?): String
{
    return when (currentRoute)
    {
        BottomNavItems.Home.route -> "Events"
        BottomNavItems.Discover.route -> "Discover"
        BottomNavItems.Contact.route -> "Contact"
        BottomNavItems.Profile.route -> "Profile"
        Screen.CreateEventScreen.route, Screen.CreateEventScreen2.route -> "Create Event"
        Screen.EventScreenDetail.route, Screen.EventBookingScreen.route -> "Event"
        else -> ""
    }
}