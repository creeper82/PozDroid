package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.SharedPrefUtils
import me.zhanghai.compose.preference.textFieldPreference

@Composable
fun PozDroidIntroScreen(
    modifier: Modifier = Modifier,
    onAddressSelected: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(16.dp),
        ) {
            WelcomeAndLogo(modifier = Modifier.padding(16.dp))
        }
        Spacer(
            modifier = Modifier.height(64.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_requires_poznode_configure_the_connection),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            AddressSettings(
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(
                modifier = Modifier.height(32.dp)
            )
            Button(
                onClick = onAddressSelected,
            ) {
                Text(
                    text = stringResource(R.string.connect_and_continue),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun AddressSettings(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        textFieldPreference(
            key = SharedPrefUtils.SERVER_ADDRESS,
            defaultValue = SharedPrefUtils.Defaults.SERVER_ADDRESS_DEFAULT,
            title = { Text(stringResource(R.string.server_address)) },
            textToValue = parseUrl,
            summary = { Text(text = it) }
        )
    }
}

@Composable
fun WelcomeAndLogo(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.application_logo),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
        Text(
            text = stringResource(R.string.welcome),
            fontSize = 32.sp
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun IntroPreview() {
    PozDroidIntroScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}