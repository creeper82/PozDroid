package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.creeper82.pozdroid.SharedPrefUtils
import com.creeper82.pozdroid.R
import me.zhanghai.compose.preference.footerPreference
import me.zhanghai.compose.preference.preferenceCategory
import me.zhanghai.compose.preference.sliderPreference
import me.zhanghai.compose.preference.textFieldPreference

val parseUrl: (String) -> String = { url ->
    val trimmed = url.trim()
    if (trimmed.endsWith("/")) trimmed.dropLast(1) else trimmed
}

@Composable
fun PozDroidSettingsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        preferenceCategory(
            key = "server",
            title = { Text(stringResource(R.string.server_configuration)) },
            modifier = Modifier
        )

        textFieldPreference(
            key = SharedPrefUtils.SERVER_ADDRESS,
            defaultValue = SharedPrefUtils.Defaults.SERVER_ADDRESS_DEFAULT,
            title = { Text(stringResource(R.string.server_address)) },
            textToValue = parseUrl,
            summary = { Text(text = it) }
        )

        sliderPreference(
            key = SharedPrefUtils.REFRESH_FREQUENCY,
            defaultValue = SharedPrefUtils.Defaults.REFRESH_FREQUENCY_DEFAULT,
            title = { Text(stringResource(R.string.refresh_departures)) },
            summary = { Text(text = stringResource(R.string.every_seconds, it.toInt())) },
            valueRange = 10f..30f,
            valueSteps = 3
        )

        footerPreference(
            key = "footer",
            summary = { Text(stringResource(R.string.you_are_using_pozdroid_v1_0)) }
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PozDroidSettingsScreen(
        modifier = Modifier.fillMaxSize()
    )
}