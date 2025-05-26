package com.creeper82.pozdroid

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.ui.screens.PozDroidApp
import com.creeper82.pozdroid.ui.theme.PozDroidTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PozDroidTheme {
                ProvidePreferenceLocals {
                    PozDroidApp()
                }
            }
        }

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val address =
            prefs.getString(PrefKeys.SERVER_ADDRESS, PrefKeys.Defaults.SERVER_ADDRESS_DEFAULT)!!

        PozNodeApiClient.refreshInstance(address)
    }
}