package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.types.Bollard
import com.creeper82.pozdroid.types.DirectionWithStops
import com.creeper82.pozdroid.ui.SearchFailed
import com.creeper82.pozdroid.ui.viewmodels.LineViewModel

@Composable
fun PozDroidLineScreen(
    lineName: String,
    modifier: Modifier = Modifier,
    viewModel: LineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var displayStops by rememberSaveable { mutableStateOf<Array<Bollard>?>(null) }

    LaunchedEffect(lineName) {
        viewModel.fetchData(lineName)
    }

    Column(modifier = modifier) {
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (uiState.isError) {
            SearchFailed()
        } else {
            LineHeader(
                lineName = lineName,
                directions = uiState.directions,
                onDirectionSelected = { displayStops = it.bollards },
                modifier = Modifier.fillMaxWidth()
            )
            displayStops?.let { stops ->
                stops.forEach { stop ->
                    Text(stop.name)
                }
            }
        }
    }
}

@Composable
fun LineHeader(
    lineName: String,
    directions: Array<DirectionWithStops>,
    modifier: Modifier = Modifier,
    onDirectionSelected: (direction: DirectionWithStops) -> Unit = {}
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectedDirection by remember { mutableStateOf<DirectionWithStops?>(null) }

    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { dropDownExpanded = true })
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                lineName,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.width(16.dp))

            Text(
                selectedDirection?.direction ?: "Direction",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.weight(1f)
            )

            Icon(Icons.Filled.ArrowDropDown, stringResource(R.string.expand_directions_arrow))

            DropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false })
            {
                directions.forEach { dir ->
                    DropdownMenuItem(
                        text = { Text(dir.direction) },
                        onClick = {
                            selectedDirection = dir
                            onDirectionSelected(dir)
                            dropDownExpanded = false
                        }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PozDroidLineScreenPreview() {
    PozDroidLineScreen(
        lineName = "2",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}