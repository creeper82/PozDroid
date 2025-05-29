package com.creeper82.pozdroid

import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPrefUtils {
    const val SERVER_ADDRESS = "server_address"
    const val REFRESH_FREQUENCY = "refresh_frequency"
    const val SHOW_INTRO = "intro_finished"
    const val USE_FAKE_DATA = "use_fake_data"

    object Defaults {
        const val SERVER_ADDRESS_DEFAULT = "http://localhost:5000/api"
        const val REFRESH_FREQUENCY_DEFAULT = 10f
        const val SHOW_INTRO_DEFAULT = true
        const val USE_FAKE_DATA_DEFAULT = false
    }

    fun setString(prefs: SharedPreferences, key: String, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }

    fun setBool(prefs: SharedPreferences, key: String, value: Boolean) {
        prefs.edit {
            putBoolean(key, value)
        }
    }

    fun setFloat(prefs: SharedPreferences, key: String, value: Float) {
        prefs.edit {
            putFloat(key, value)
        }
    }
}