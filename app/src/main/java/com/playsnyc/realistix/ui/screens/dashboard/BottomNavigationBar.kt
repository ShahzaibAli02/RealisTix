package com.playsnyc.realistix.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.playsnyc.realistix.data.enums.BottomNavItems
import com.playsnyc.realistix.ui.theme.MyPerColors

@Composable fun BottomNavigationBar(navController: NavController)
{
    val typography = MaterialTheme.typography
    BottomNavigation(backgroundColor = MyPerColors._FFFFFF) {
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
                                modifier = Modifier.size(
                                        width = 40.dp,
                                        height = 40.dp
                                ),
                                painter = painterResource(id = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon),
                                contentDescription = null
                        )
                    },
                    label = {
                        Text(
                                text = item.name,
                                style = typography.titleSmall
                        )

                    })
        }
    }
}
