package com.creeper82.pozdroid.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.creeper82.pozdroid.R

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
    val showIntro = true;
    Scaffold(
        topBar = {
            PozDroidHeader()
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (showIntro) PozDroidScreen.Intro.name else PozDroidScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = PozDroidScreen.Home.name) {
                PozDroidHomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = PozDroidScreen.Intro.name) {
                PozDroidIntroScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onAddressSelected = { address ->
                        navController.navigate(PozDroidScreen.Home.name)
                    }
                )
            }
        }
    }
}