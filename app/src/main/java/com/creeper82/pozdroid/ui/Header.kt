package com.creeper82.pozdroid.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.creeper82.pozdroid.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PozDroidHeader(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name), modifier = modifier)
        }
    )
}