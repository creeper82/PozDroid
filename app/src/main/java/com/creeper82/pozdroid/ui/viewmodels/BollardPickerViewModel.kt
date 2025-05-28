package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.types.BollardWithDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BollardPickerUiState(
    val bollards: Array<BollardWithDirections> = emptyArray(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

class BollardPickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BollardPickerUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun search(stopName: String) {
        setError(false); setLoading(true)

        try {
            val bollards = PozNodeApiClient.getApi().getBollards(stopName)
            setBollards(bollards)
        } catch (e: Exception) {
            setError(true)
        } finally {
            setLoading(false)
        }
    }

    private fun setBollards(value: Array<BollardWithDirections>) {
        _uiState.update { current ->
            current.copy(bollards = value)
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