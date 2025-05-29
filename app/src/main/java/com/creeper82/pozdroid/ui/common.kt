package com.creeper82.pozdroid.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.R

@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = modifier)
}

@Composable
fun SearchFailed(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.failed_to_search_are_you_connected),
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ResultRow(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconDescription: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                icon,
                iconDescription,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(text)
    }
}