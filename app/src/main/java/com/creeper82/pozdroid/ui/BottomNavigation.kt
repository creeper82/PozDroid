package com.creeper82.pozdroid.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.creeper82.pozdroid.ui.screens.PozDroidScreen

data class TopLevelRoute<T : Any>(val label: String, val route: T, val icon: ImageVector)


val bottomNavItems = arrayOf<TopLevelRoute<String>>(
    TopLevelRoute("Home", PozDroidScreen.Home.name, Icons.Filled.Home),
    TopLevelRoute("Search", PozDroidScreen.Search.name, Icons.Filled.Search),
    TopLevelRoute("More", PozDroidScreen.Settings.name, Icons.Filled.Menu),
)

@Composable
fun PozDroidBottomNav(navController: NavController, modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    NavigationBar {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}