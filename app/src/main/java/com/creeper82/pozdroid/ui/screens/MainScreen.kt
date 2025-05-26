package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.creeper82.pozdroid.ui.PozDroidBottomNav
import com.creeper82.pozdroid.ui.PozDroidHeader

enum class PozDroidScreen {
    Intro,
    Home,
    Departures,
    Line,
    Search,
    Settings
}

@Composable
fun PozDroidApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var showIntro by rememberSaveable { mutableStateOf(true) }
    val screenModifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    Scaffold(
        topBar = {
            PozDroidHeader()
        },
        modifier = modifier,
        bottomBar = { if (!showIntro) PozDroidBottomNav(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (showIntro) PozDroidScreen.Intro.name else PozDroidScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = PozDroidScreen.Intro.name) {
                PozDroidIntroScreen(
                    modifier = screenModifier,
                    onAddressSelected = {
                        navController.navigate(PozDroidScreen.Home.name)
                        showIntro = false
                    }
                )
            }
            composable(route = PozDroidScreen.Home.name) {
                PozDroidHomeScreen(
                    modifier = screenModifier
                )
            }
            composable(route = PozDroidScreen.Search.name) {
                PozDroidSearchScreen(
                    modifier = Modifier.fillMaxSize(),
                    onBollardSelected = {},
                    onLineSelected = {}
                )
            }
            composable(route = PozDroidScreen.Settings.name) {
                PozDroidSettingsScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = PozDroidScreen.Line.name) {
                PozDroidLineScreen(
                    modifier = screenModifier
                )
            }
            composable(route = PozDroidScreen.Departures.name) {
                PozDroidDeparturesScreen(
                    bollardName = "Rynek Wildecki",
                    bollardSymbol = "RYWI74",
                    announcements = emptyArray(),
                    departures = emptyArray(),
                    modifier = screenModifier
                )
            }
        }
    }
}