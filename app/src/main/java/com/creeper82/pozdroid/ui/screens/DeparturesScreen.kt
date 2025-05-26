package com.creeper82.pozdroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.types.Announcement
import com.creeper82.pozdroid.types.Departure
import com.creeper82.pozdroid.types.Vehicle

@Composable
fun PozDroidDeparturesScreen(
    bollardName: String,
    bollardSymbol: String,
    announcements: Array<Announcement>,
    departures: Array<Departure>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        StopHeader(bollardName, bollardSymbol, Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        AnnouncementsAndDeparturesCard(
            announcements = announcements,
            departures = departures,
            modifier = Modifier.fillMaxWidth()
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

    Column(modifier = modifier.clickable(onClick = {expanded = !expanded}).padding(16.dp)) {
        if (announcements.any()) {
            Row {
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
                Column {
                    announcements.forEach { announcement ->
                        Announcement(announcement)
                        HorizontalDivider(modifier = Modifier.padding(8.dp))
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

@Composable
fun AnnouncementsAndDeparturesCard(
    announcements: Array<Announcement>,
    departures: Array<Departure>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column {
            ExpandableAnnouncements(announcements, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))

            Departures(
                departures,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PozDroidDeparturesScreenPreview() {
    PozDroidDeparturesScreen(
        bollardName = "Rynek Wildecki",
        bollardSymbol = "RYWI74",
        announcements = arrayOf(
            Announcement(
                content = "Uwaga: Przykladowe ogloszenie!!!",
                startDate = "26.05.2025",
                endDate = "26.05.2025"
            ),
            Announcement(
                content = "W dniu x zgodnie z y zostanie zamknięta trasa z dla linii należących coś tam",
                startDate = "01.01.2026",
                endDate = "02.01.2026"
            )
        ),
        departures = arrayOf(
            Departure(
                line = "2",
                direction = "Ogrody",
                departure = "09:14",
                minutes = 3,
                realTime = true,
                onStopPoint = false,
                vehicle = Vehicle(
                    id = "511",
                    airConditioned = null,
                    bike = null,
                    chargers = null,
                    lowFloor = null,
                    lowEntrance = null,
                    ramp = null,
                    ticketMachine = null
                )
            ),
            Departure(
                line = "8",
                direction = "Miłostowo",
                departure = "09:15",
                minutes = 4,
                realTime = true,
                onStopPoint = false,
                vehicle = Vehicle(
                    id = "513",
                    airConditioned = null,
                    bike = null,
                    chargers = null,
                    lowFloor = null,
                    lowEntrance = null,
                    ramp = null,
                    ticketMachine = null
                )
            ),
            Departure(
                line = "PKM3",
                direction = "Os. Wichrowe Wzgórze Dodatkowe",
                departure = "09:16",
                minutes = 5,
                realTime = false,
                onStopPoint = false,
                vehicle = Vehicle(
                    id = "1023",
                    airConditioned = null,
                    bike = null,
                    chargers = null,
                    lowFloor = null,
                    lowEntrance = null,
                    ramp = null,
                    ticketMachine = null
                )
            ),
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}