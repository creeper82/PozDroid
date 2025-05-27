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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.creeper82.pozdroid.R
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

    var query by remember { mutableStateOf("") }

    LaunchedEffect(query) {
        searchViewModel.search(query)
    }

    Column(modifier = modifier) {
        SearchTextField(
            onSearch = { q -> query = q },
            onBollardSelected = onBollardSelected,
            onLineSelected = onLineSelected,
            searchResultsBollards = stops,
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
    onBollardSelected: (bollardSymbol: String) -> Unit,
    onLineSelected: (lineName: String) -> Unit,
    searchResultsBollards: List<String>,
    searchResultsLines: List<String>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isError: Boolean = false
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var searchMode by rememberSaveable { mutableStateOf(SearchMode.Stops) }

    val results =
        if (searchMode == SearchMode.Stops) searchResultsBollards
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
                    onQueryChange = { q ->
                        searchQuery = q
                    },
                    onSearch = {},
                    placeholder = { Text("Search") },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    leadingIcon = { Icon(Icons.Filled.Search, "Search icon") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            StopsLinesSegmentedButtons(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSelection = { searchMode = it }
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
                        SearchResult(searchMode, result, onClick = {
                            if (searchMode == SearchMode.Stops) onBollardSelected(result)
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
        if (searchMode == SearchMode.Stops) "Bus stop icon"
        else "Line icon"

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
        text = "Failed to search. Are you connected?",
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
    onSelection: (selection: SearchMode) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
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
                    selectedIndex = index
                    onSelection(searchModes[selectedIndex])
                },
                selected = index == selectedIndex,
                label = { Text(mode.name) },
            )
        }
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