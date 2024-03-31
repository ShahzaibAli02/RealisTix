package com.playsnyc.realistix.data.enums

import com.playsnyc.realistix.R

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