package com.creeper82.pozdroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.creeper82.pozdroid.types.DirectionWithStops
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LineHeaderUiState(
    val isExpanded: Boolean = false,
    val selectedDirection: DirectionWithStops? = null
)

class LineHeaderViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LineHeaderUiState())
    var uiState = _uiState.asStateFlow()

    fun setExpanded(value: Boolean) {
        _uiState.update { current ->
            current.copy(isExpanded = value)
        }
    }

    fun setSelectedDirection(value: DirectionWithStops) {
        _uiState.update { current ->
            current.copy(selectedDirection = value)
        }
    }
}