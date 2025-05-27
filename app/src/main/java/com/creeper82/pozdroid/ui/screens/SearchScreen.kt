package com.creeper82.pozdroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.creeper82.pozdroid.R
import com.creeper82.pozdroid.types.BollardWithDirections
import com.creeper82.pozdroid.ui.Header
import com.creeper82.pozdroid.ui.viewmodels.BollardPickerViewModel
import com.creeper82.pozdroid.ui.viewmodels.SearchBarViewModel
import com.creeper82.pozdroid.ui.viewmodels.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun PozDroidSearchScreen(
    modifier: Modifier = Modifier,
    onBollardSelected: (bollardSymbol: String) -> Unit,
    onLineSelected: (lineName: String) -> Unit,
    searchViewModel: SearchViewModel = viewModel()
) {
    val uiState by searchViewModel.uiState.collectAsState()

    val lines = uiState.searchResultsLines
    val stops = uiState.searchResultsStops
    val loading = uiState.isLoading
    val error = uiState.isError
    val query = uiState.query
    val bottomSheet = uiState.bottomSheetVisible
    val bottomSheetStopName = uiState.bottomSheetStopName

    LaunchedEffect(query) {
        searchViewModel.search(query)
    }

    if (bottomSheet) {
        BollardPickerSheet(
            stopName = bottomSheetStopName,
            onBollardSelected = {
                searchViewModel.dismissBollardPicker()
                onBollardSelected(it)
            },
            onDismiss = { searchViewModel.dismissBollardPicker() }
        )
    }

    Column(modifier = modifier) {
        SearchTextField(
            onSearch = { searchViewModel.updateQuery(it) },
            onStopSelected = { searchViewModel.displayBollardPicker(it) },
            onLineSelected = onLineSelected,
            searchResultsBollards = stops.map { it.name }.toTypedArray(),
            searchResultsLines = lines,
            isLoading = loading,
            isError = error
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    onSearch: (newQuery: String) -> Unit,
    onStopSelected: (stopName: String) -> Unit,
    onLineSelected: (lineName: String) -> Unit,
    searchResultsBollards: Array<String>,
    searchResultsLines: Array<String>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false,
    viewModel: SearchBarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val searchQuery = uiState.query
    val expanded = uiState.expanded

    val mode = uiState.mode

    val results =
        if (mode == SearchMode.Stops) searchResultsBollards
        else searchResultsLines

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            delay(800)
            onSearch(searchQuery)
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = { viewModel.updateQuery(it) },
                    onSearch = {},
                    placeholder = { Text("Search") },
                    expanded = expanded,
                    onExpandedChange = { viewModel.updateExpanded(it) },
                    leadingIcon = { Icon(Icons.Filled.Search, "Search icon") }
                )
            },
            expanded = expanded,
            onExpandedChange = { viewModel.updateExpanded(it) },
        ) {
            StopsLinesSegmentedButtons(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                selectedMode = mode,
                onSelection = { viewModel.updateSearchMode(it) }
            )
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (results.any() && !isError) {
                    results.forEach { result ->
                        SearchResult(mode, result, onClick = {
                            if (mode == SearchMode.Stops) onStopSelected(result)
                            else onLineSelected(result)
                        })
                    }
                } else if (isError) {
                    SearchFailed()
                } else if (!isLoading) {
                    NoSearchResults()
                }
            }
        }
    }
}

@Composable
fun SearchResult(
    searchMode: SearchMode,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val iconRes =
        if (searchMode == SearchMode.Stops) Icons.Default.PinDrop
        else Icons.Default.Polyline

    val iconDesc =
        if (searchMode == SearchMode.Stops) stringResource(R.string.bus_stop_icon)
        else stringResource(R.string.line_icon)

    SearchResultRow(
        text = text,
        icon = iconRes,
        iconDescription = iconDesc,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun NoSearchResults(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.no_results),
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
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
fun SearchResultRow(
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
            .padding(16.dp)
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

enum class SearchMode {
    Stops,
    Lines
}

@Composable
fun StopsLinesSegmentedButtons(
    modifier: Modifier = Modifier,
    selectedMode: SearchMode,
    onSelection: (selection: SearchMode) -> Unit
) {
    val searchModes = SearchMode.entries

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        searchModes.forEachIndexed { index, mode ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = searchModes.size,
                    baseShape = ShapeDefaults.Small
                ),
                onClick = {
                    onSelection(mode)
                },
                selected = selectedMode == mode,
                label = { Text(mode.name) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BollardPickerSheet(
    stopName: String,
    onBollardSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BollardPickerViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(stopName) {
        viewModel.search(stopName)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Header(stopName, Modifier.padding(16.dp))

        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        } else if (uiState.isError) {
            Text(stringResource(R.string.failed_to_load_the_bollards_list), Modifier.padding(16.dp))
        } else if (uiState.bollards.any()) {
            uiState.bollards.forEach { bollard ->
                BollardPickerItem(
                    bollard,
                    onClick = { onBollardSelected(bollard.symbol) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            NoSearchResults(Modifier.padding(16.dp))
        }
    }
}

@Composable
fun BollardPickerItem(
    bollard: BollardWithDirections,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(bollard.symbol)
        Text(
            text = bollard.directions.joinToString(",  ") { "${it.lineName}\u00A0=>\u00A0${it.direction}" },
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun SearchScreenPreview() {
    PozDroidSearchScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        onBollardSelected = {},
        onLineSelected = {}
    )
}