package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.services.impl.DatabaseHelper
import com.creeper82.pozdroid.types.Favorite
import com.creeper82.pozdroid.ui.Header
import com.creeper82.pozdroid.ui.ResultRow

@Composable
fun PozDroidHomeScreen(
    onFavoriteSelected: (symbol: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites by DatabaseHelper.getFavorites().collectAsState(emptyList())

    Column(
        modifier = modifier,
    ) {
        if (favorites.any()) {
            Favorites(favorites, onClick = { onFavoriteSelected(it.bollardSymbol) })
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
fun Favorites(
    favorites: List<Favorite>,
    onClick: (fav: Favorite) -> Unit,
    modifier: Modifier = Modifier
) {
    Header("Favorites", modifier = modifier)

    favorites.forEach { f ->
        Favorite(f, onClick = { onClick(f) })
    }
}

@Composable
fun Favorite(
    favorite: Favorite,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResultRow(
        text = favorite.name,
        modifier = modifier,
        icon = Icons.Default.Star,
        onClick = onClick,
        iconDescription = stringResource(R.string.favorite_icon)
    )
}