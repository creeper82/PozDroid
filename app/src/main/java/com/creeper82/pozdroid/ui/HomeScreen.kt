package com.creeper82.pozdroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.types.FavoriteBollard

@Composable
fun PozDroidHomeScreen(
    modifier: Modifier = Modifier,
    favoriteBollards: Array<FavoriteBollard> = emptyArray()
) {
    Column(
        modifier = modifier,
    ) {
        if (favoriteBollards.any()) {
            Favorites(favoriteBollards)
        } else {
            Welcome()
        }
    }
}

@Composable
fun Welcome(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Header(stringResource(R.string.welcome))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.your_favs_will_appear_here))
    }
}

@Composable
fun Favorites(favoriteBollards: Array<FavoriteBollard>, modifier: Modifier = Modifier) {
    Header("Favorites", modifier = modifier)
}