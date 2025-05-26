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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.R

@Composable
fun PozDroidIntroScreen(
    modifier: Modifier = Modifier,
    onAddressSelected: (address: String) -> Unit = {}
) {
    var address = ""

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
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
            AddressTextField(
                modifier = Modifier.fillMaxWidth(),
                onAddressChanged = { newAddress ->
                    address = newAddress
                },
                onSubmit = { onAddressSelected(address) }
            )
            Spacer(
                modifier = Modifier.height(32.dp)
            )
            Button(
                onClick = { onAddressSelected(address) },
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
fun AddressTextField(
    modifier: Modifier = Modifier,
    onAddressChanged: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    var address by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = address,
        onValueChange = { newText ->
            address = newText
            onAddressChanged(newText.text)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Go,
        ),
        keyboardActions = KeyboardActions(
            onGo = { onSubmit() }
        ),
        label = { Text(text = stringResource(R.string.server_address)) },
        placeholder = { Text(text = stringResource(R.string.localhost_placeholder_address)) },
        modifier = modifier
    )
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