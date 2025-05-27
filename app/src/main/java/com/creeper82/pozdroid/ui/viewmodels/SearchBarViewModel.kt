package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.ui.screens.SearchMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchBarUiState(
    val query: String = "",
    val expanded: Boolean = false,
    val mode: SearchMode = SearchMode.Stops
)

class SearchBarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchBarUiState())
    var uiState = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.update { current ->
            current.copy(query = query)
        }
    }

    fun updateSearchMode(mode: SearchMode) {
        _uiState.update { current ->
            current.copy(mode = mode)
        }
    }

    fun updateExpanded(expanded: Boolean) {
        _uiState.update { current ->
            current.copy(expanded = expanded)
        }
    }
}