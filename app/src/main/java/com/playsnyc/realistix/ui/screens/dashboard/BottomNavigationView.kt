package com.playsnyc.realistix.ui.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.playsnyc.realistix.data.enums.BottomNavItems
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.contact.ContactScreen
import com.playsnyc.realistix.ui.screens.discover.DiscoverScreen
import com.playsnyc.realistix.ui.screens.createevent.CreateEventScreen
import com.playsnyc.realistix.ui.screens.createevent.CreateEventScreen2
import com.playsnyc.realistix.ui.screens.home.EventBookingScreen
import com.playsnyc.realistix.ui.screens.home.EventDetailScreen
import com.playsnyc.realistix.ui.screens.home.HomeScreen
import com.playsnyc.realistix.ui.screens.myactivities.MyActivitiesScreen
import com.playsnyc.realistix.ui.screens.myconnections.MyConnectionScreen
import com.playsnyc.realistix.ui.screens.profile.ProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable fun BottomNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLogOut:()->Unit,
)
{
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = BottomNavItems.Home.route
    ) {
        composable(BottomNavItems.Home.route) {
            HomeScreen(
                    navController,
                    viewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
//        composable(BottomNavItems.Discover.route) {
//            DiscoverScreen()
//        }
        composable(BottomNavItems.Contact.route) {
            MyConnectionScreen(navController)
        }
        composable(Screen.ConnectionsScreen.route) {
            ContactScreen(navController)
        }
        composable(Screen.MyActivitiesScreen.route+"/{uid}/{saved}") {
            val uid=it.arguments?.getString("uid")
            val saved=it.arguments?.getString("saved")
            MyActivitiesScreen(uid!!,saved=="true")
        }
        composable(BottomNavItems.Profile.route) {
            ProfileScreen(navController,onLogOut=onLogOut)
        }

        composable(Screen.EventScreenDetail.route) {
            EventDetailScreen(
                    navController,
                    viewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }
        composable(Screen.CreateEventScreen.route+"?eventDocId={eventDocId}",
                arguments = listOf(navArgument("eventDocId") { defaultValue = "" }

                )) {
            CreateEventScreen(
                    navController,
                    viewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner),
                    bundle =it.arguments
            )
        }
        composable(Screen.CreateEventScreen2.route) {
            CreateEventScreen2(
                    navController,
                    viewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }

        composable(Screen.EventBookingScreen.route) {
            EventBookingScreen(
                    navController,
                    viewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
            )
        }



    }
}