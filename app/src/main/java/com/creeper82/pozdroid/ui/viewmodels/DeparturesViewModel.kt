package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.services.impl.DatabaseHelper
import com.creeper82.pozdroid.services.impl.PozNodeApiClient
import com.creeper82.pozdroid.types.Favorite
import com.creeper82.pozdroid.types.responses.DeparturesResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DeparturesUiState(
    val departures: DeparturesResponse? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isFavorite: Boolean = false
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
        } catch (e: CancellationException) {
        } catch (e: Exception) {
            setError(true)
        } finally {
            setLoading(false)
        }
    }

    suspend fun fetchFavoriteStatus(bollardSymbol: String) {
        setIsFavorite(DatabaseHelper.isFavorite(bollardSymbol))
    }

    suspend fun toggleFavorite(bollardSymbol: String, favName: String) {
        if (DatabaseHelper.isFavorite(bollardSymbol)) {
            DatabaseHelper.deleteFavoriteByBollardSymbol(bollardSymbol)
            setIsFavorite(false)
        } else {
            DatabaseHelper.addFavorite(Favorite(favName, bollardSymbol))
            setIsFavorite(true)
        }
    }

    suspend fun deleteFavorite(bollardSymbol: String) {
        DatabaseHelper.deleteFavoriteByBollardSymbol(bollardSymbol)
        setIsFavorite(false)
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

    private fun setIsFavorite(value: Boolean) {
        _uiState.update { current ->
            current.copy(isFavorite = value)
        }
    }
}