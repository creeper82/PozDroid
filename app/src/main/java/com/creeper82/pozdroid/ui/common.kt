package com.creeper82.pozdroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = modifier)
}

@Composable
fun Padding(value: Dp, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier.padding(value)) {
        content()
    }
}