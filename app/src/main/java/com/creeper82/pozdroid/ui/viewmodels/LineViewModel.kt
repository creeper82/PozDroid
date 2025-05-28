package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.types.responses.LineStopsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LineUiState(
    val directions: LineStopsResponse = emptyArray(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class LineViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LineUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun fetchData(lineName: String) {
        setLoading(true)

        try {
            val response = PozNodeApiClient.getApi().getLine(lineName)
            setError(false)
            setResponse(response)
        } catch (e: Exception) {
            setError(true)
        } finally {
            setLoading(false)
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

    private fun setResponse(value: LineStopsResponse) {
        _uiState.update { current ->
            current.copy(directions = value)
        }
    }
}