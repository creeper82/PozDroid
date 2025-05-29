package com.creeper82.pozdroid.ui.screens

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.SharedPrefUtils
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
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
    val context = LocalContext.current
    val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val canGoBack = navController.previousBackStackEntry != null

    var showIntro by rememberSaveable {
        mutableStateOf(
            sharedPrefs.getBoolean(
                SharedPrefUtils.SHOW_INTRO,
                SharedPrefUtils.Defaults.SHOW_INTRO_DEFAULT
            )
        )
    }

    val screenModifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    Scaffold(
        topBar = {
            PozDroidHeader(
                title = currentRoute?.substringBefore("/") ?: stringResource(R.string.app_name),
                canGoBack = canGoBack,
                onBack = { navController.popBackStack() }
            )
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

                        SharedPrefUtils.setBool(sharedPrefs, SharedPrefUtils.SHOW_INTRO, false)
                        PozNodeApiClient.reloadBasedOnPrefs(context)
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
                    onBollardSelected = {
                        navController.navigate("${PozDroidScreen.Departures.name}/$it")
                    },
                    onLineSelected = {
                        navController.navigate("${PozDroidScreen.Line.name}/$it")
                    }
                )
            }
            composable(route = PozDroidScreen.Settings.name) {
                PozDroidSettingsScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = "${PozDroidScreen.Line.name}/{id}") {
                PozDroidLineScreen(
                    lineName = it.arguments?.getString("id") ?: "",
                    modifier = screenModifier,
                    onBollardSelected = {
                        navController.navigate("${PozDroidScreen.Departures.name}/$it")
                    }
                )
            }
            composable(route = "${PozDroidScreen.Departures.name}/{id}") {
                var refreshInterval by remember { mutableStateOf(SharedPrefUtils.Defaults.REFRESH_FREQUENCY_DEFAULT) }

                LaunchedEffect(Unit) {
                    refreshInterval = sharedPrefs.getFloat(
                        SharedPrefUtils.REFRESH_FREQUENCY,
                        SharedPrefUtils.Defaults.REFRESH_FREQUENCY_DEFAULT
                    )
                }

                PozDroidDeparturesScreen(
                    bollardSymbol = it.arguments?.getString("id") ?: "",
                    refreshFrequencySeconds = refreshInterval.toInt(),
                    modifier = screenModifier
                )
            }
        }
    }
}