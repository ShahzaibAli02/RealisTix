package com.playsnyc.realistix.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.playsnyc.realistix.R
import com.playsnyc.realistix.ui.screens.home.HomeScreen
import com.playsnyc.realistix.ui.screens.profile.ProfileScreen
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts

@Composable fun DashBoardScreen(mainNavController: NavHostController)
{
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomNavigationBar(navController)
    }) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)){
            Header()
            BottomNavigationView(
                    modifier = Modifier.weight(1f),
                    navController = navController
            )
        }

    }
}

@Composable
fun Header()
{
    Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
    )
    Spacer(modifier = Modifier.height(10.dp))
    Divider()
}

@Composable fun BottomNavigationBar(navController: NavController)
{
    BottomNavigation(backgroundColor = MyPerColors._FFFFFF)
    {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavItems.entries.forEach { item ->
            BottomNavigationItem(selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Image(
                                modifier = Modifier.size(width=40.dp,height=60.dp),
                                painter = painterResource(id = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon),
                                contentDescription = null
                        )
                    },
                    label = {
                        Text( text = item.name, fontFamily = MyFonts.poppins(), fontWeight = FontWeight.Normal)


                    })
        }
    }
}

@Composable fun BottomNavigationView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
)
{
    NavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = BottomNavItems.Home.route
    ) {
        composable(BottomNavItems.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItems.Discover.route) {
            HomeScreen()
        }
        composable(BottomNavItems.Contact.route) {
            HomeScreen()
        }
        composable(BottomNavItems.Profile.route) {
            ProfileScreen()
        }


    }
}

@Preview(showBackground = true) @Composable fun DashBoardScreenPrev()
{
    RealisTixTheme {
        DashBoardScreen(NavHostController(LocalContext.current))
    }
}

enum class BottomNavItems(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: String,
)
{
    Home(
            "Home",
            R.drawable.home_selected,
            R.drawable.home_unselected,
            "Home"
    ),
    Discover(
            "Discover",
            R.drawable.discover_selected,
            R.drawable.discover_unselected,

            "Discover"
    ),
    Contact(
            "Contact",
            R.drawable.contact_selected,
            R.drawable.contact_unselected,
            "Contact"
    ),
    Profile(
            "Profile",
            R.drawable.profile_selected,
            R.drawable.profile_unselected,
            "Profile"
    ),
}