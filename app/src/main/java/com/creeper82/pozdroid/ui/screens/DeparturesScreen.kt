package com.creeper82.pozdroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.types.Announcement
import com.creeper82.pozdroid.types.Departure
import com.creeper82.pozdroid.ui.SearchFailed
import com.creeper82.pozdroid.ui.viewmodels.DeparturesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

@Composable
fun PozDroidDeparturesScreen(
    bollardSymbol: String,
    modifier: Modifier = Modifier,
    refreshFrequencySeconds: Int = 10,
    viewModel: DeparturesViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val loading = uiState.isLoading
    val error = uiState.isError
    val response = uiState.departures
    val stopName = response?.bollardName

    val reloadScope = rememberCoroutineScope()
    val reloadSemaphore = remember { Semaphore(1) }

    suspend fun refresh() = reloadSemaphore.withPermit {
        viewModel.fetchData(bollardSymbol)
    }

    LaunchedEffect(bollardSymbol, refreshFrequencySeconds) {
        while (true) {
            refresh()
            delay(refreshFrequencySeconds * 1000L)
        }
    }

    Column(modifier = modifier) {
        StopHeader(stopName ?: "Loading", bollardSymbol, Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        AnnouncementsAndDeparturesCard(
            loading = loading,
            error = error,
            announcements = response?.announcements ?: emptyArray(),
            departures = response?.departures ?: emptyArray(),
            onRefreshRequest = { reloadScope.launch { refresh() } },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StopHeader(
    name: String,
    symbol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                name, style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
            Text(
                symbol, style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun ExpandableAnnouncements(announcements: Array<Announcement>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    val columnModifier = if (announcements.any()) modifier.clickable(onClick = {
        expanded = !expanded
    }) else modifier

    if (announcements.isEmpty()) expanded = false

    Column(
        modifier = columnModifier
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 0.dp
            )
    ) {
        if (announcements.any()) {
            Row(Modifier.padding(bottom = 8.dp)) {
                Text(
                    stringResource(R.string.announcements, announcements.size),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    "Expand announcements arrow"
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(horizontal = 8.dp)) {
                    announcements.forEach { announcement ->
                        Announcement(announcement)
                        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        } else {
            Text(stringResource(R.string.no_announcements))
        }
    }
}

@Composable
fun Announcement(announcement: Announcement, modifier: Modifier = Modifier) {
    Text(
        text = "${announcement.content} (${announcement.startDate}\u00A0-\u00A0${announcement.endDate})",
        modifier = modifier
    )
}

@Composable
fun Departures(
    departures: Array<Departure>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        departures.forEach { departure ->
            Departure(
                departure, modifier = Modifier
                    .clickable(onClick = {})
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun Departure(
    departure: Departure,
    modifier: Modifier = Modifier
) {
    val boldLargeStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    val largeStyle = TextStyle(
        fontSize = 18.sp
    )
    val boldLargePrimaryColorStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    val timePrefix = if (departure.realTime) "" else "~"
    val timeText = timePrefix + if (departure.minutes == 0) "<1 min" else "${departure.minutes} min"

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = departure.line,
            style = boldLargeStyle,
            modifier = Modifier.widthIn(min = 30.dp),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = departure.direction,
            style = boldLargeStyle,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = timeText,
            style = if (departure.realTime) boldLargePrimaryColorStyle else largeStyle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsAndDeparturesCard(
    announcements: Array<Announcement>,
    departures: Array<Departure>,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    error: Boolean = false,
    onRefreshRequest: () -> Unit = {}
) {
    Card(modifier = modifier) {
        val pullToRefreshState = rememberPullToRefreshState()
        val noIndicator: @Composable (BoxScope.() -> Unit) = {}

        PullToRefreshBox(
            isRefreshing = loading,
            onRefresh = onRefreshRequest,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize(),
            indicator = noIndicator
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                LinearProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .alpha(pullToRefreshState.distanceFraction)
                )

                if (error) {
                    SearchFailed()
                } else {
                    ExpandableAnnouncements(announcements, modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(8.dp))

                    Departures(
                        departures,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PozDroidDeparturesScreenPreview() {
    PozDroidDeparturesScreen(
        bollardSymbol = "RYWI74",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}