package com.creeper82.pozdroid

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
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
            prefs.getString(
                SharedPrefUtils.SERVER_ADDRESS,
                SharedPrefUtils.Defaults.SERVER_ADDRESS_DEFAULT
            )!!

        try {
            PozNodeApiClient.refreshInstance(address)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "URL failure. Verify that the server URL is correct and valid!",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}