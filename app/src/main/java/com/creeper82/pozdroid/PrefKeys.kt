package com.creeper82.pozdroid

object PrefKeys {
    const val SERVER_ADDRESS = "server_address"
    const val REFRESH_FREQUENCY = "refresh_frequency"

    object Defaults {
        const val SERVER_ADDRESS_DEFAULT = "http://localhost:5000"
        const val REFRESH_FREQUENCY_DEFAULT = 10f
    }
}