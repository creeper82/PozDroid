package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.types.responses.DeparturesResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DeparturesUiState(
    val departures: DeparturesResponse? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

class DeparturesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DeparturesUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun fetchData(bollardSymbol: String) {
        setLoading(true)

        try {
            val response = PozNodeApiClient.getApi().getDepartures(bollardSymbol)
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

    private fun setResponse(value: DeparturesResponse) {
        _uiState.update { current ->
            current.copy(departures = value)
        }
    }
}