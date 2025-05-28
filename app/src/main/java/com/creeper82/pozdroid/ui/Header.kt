package com.creeper82.pozdroid.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.creeper82.pozdroid.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PozDroidHeader(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
    canGoBack: Boolean = false,
    onBack: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            if (canGoBack) {
                BackButton(onClick = onBack)
            }
        },
        title = {
            Text(text = title)
        },
    )
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            stringResource(R.string.go_back),
        )
    }
}