package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.types.responses.StopsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchUiState(
    val searchResultsLines: Array<String> = emptyArray(),
    val searchResultsStops: StopsResponse = emptyArray(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val query: String = "",
    val bottomSheetVisible: Boolean = false,
    val bottomSheetStopName: String = ""
)

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var lastSuccessfulQuery: String = "_NONE_"

    suspend fun search(query: String) {
        if (shouldSearch) {
            setError(false); setLoading(true)

            try {
                queryApi(query)
                lastSuccessfulQuery = query
            } catch (e: Exception) {
                setError(true)
            } finally {
                setLoading(false)
            }
        }
    }

    fun displayBollardPicker(stopName: String) {
        _uiState.update { current ->
            current.copy(
                bottomSheetStopName = stopName,
                bottomSheetVisible = true
            )
        }
    }

    fun dismissBollardPicker() {
        _uiState.update { current ->
            current.copy(
                bottomSheetVisible = false
            )
        }
    }

    val shouldSearch
        get() = _uiState.value.query != lastSuccessfulQuery

    private suspend fun queryApi(query: String) = coroutineScope {
        val stopsDeferred = async { PozNodeApiClient.getApi().getStops(query) }
        val linesDeferred = async { PozNodeApiClient.getApi().getLines(query) }

        val stops = stopsDeferred.await()
        val lines = linesDeferred.await()

        _uiState.update { current ->
            current.copy(
                searchResultsStops = stops,
                searchResultsLines = lines
            )
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { current ->
            current.copy(query = query)
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