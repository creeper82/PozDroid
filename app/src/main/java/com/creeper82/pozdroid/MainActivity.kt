package com.creeper82.pozdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.creeper82.pozdroid.services.impl.DatabaseHelper
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.ui.screens.PozDroidApp
import com.creeper82.pozdroid.ui.theme.PozDroidTheme
import kotlinx.coroutines.delay
import me.zhanghai.compose.preference.ProvidePreferenceLocals

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)
        enableEdgeToEdge()
        DatabaseHelper.init(this)
        setContent {
            PozDroidTheme {
                ProvidePreferenceLocals {
                    PozDroidApp()
                }
            }
        }

        PozNodeApiClient.reloadBasedOnPrefs(this)
    }
}