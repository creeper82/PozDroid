package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchUiState(
    val searchResultsLines: List<String> = emptyList(),
    val searchResultsStops: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    suspend fun search(query: String) {
        setError(false); setLoading(true)

        try {
            queryApi(query)
        } catch (e: Exception) {
            setError(true);
        } finally {
            setLoading(false)
        }
    }

    private suspend fun queryApi(query: String) = coroutineScope {
        val stopsDeferred = async { PozNodeApiClient.getApi().getStops(query).map { it.name } }
        val linesDeferred = async { PozNodeApiClient.getApi().getLines(query).toList() }

        val (stops, lines) = awaitAll(stopsDeferred, linesDeferred)

        _uiState.update { current ->
            current.copy(
                searchResultsStops = stops,
                searchResultsLines = lines
            )
        }
    }

    private fun setLoading(value: Boolean) {
        _uiState.update { current ->
            current.copy(isLoading = value)
        }
    }

    private fun setError(value: Boolean) {
        _uiState.update { current ->
            current.copy(isError = value)
        }
    }
}