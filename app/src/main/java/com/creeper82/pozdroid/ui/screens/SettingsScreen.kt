package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.ui.Header
import com.creeper82.pozdroid.ui.Padding

@Composable
fun PozDroidSettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Padding(16.dp) {
            Header("Settings")
        }

        SettingsSection("Server configuration")
        Setting(stringResource(R.string.server_address), "http://localhost:5000")
        Setting("Refresh departures", "every 10 seconds")
        Spacer(Modifier.height(16.dp))

        SettingsSection("About")
        Setting("PozDroid", "version 1.0")
    }
}

@Composable
fun Setting(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineContent = { Text(name) },
        supportingContent = { Text(value) },
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
fun SettingsSection(sectionName: String, modifier: Modifier = Modifier) {
    Text(
        sectionName, modifier = modifier.padding(start = 16.dp), style = TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    )
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PozDroidSettingsScreen(
        modifier = Modifier.fillMaxSize()
    )
}