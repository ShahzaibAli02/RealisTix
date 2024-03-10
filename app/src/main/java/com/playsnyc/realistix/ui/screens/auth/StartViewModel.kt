package com.playsnyc.realistix.ui.screens.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StartViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(StartScreenState())
    val uiState: StateFlow<StartScreenState> = _uiState.asStateFlow()
    fun updateProgressVisibility(visibility: Boolean)
    {
        _uiState.update { currentState ->
            currentState.copy(progressVisibility = visibility)
        }
    }
    fun update( func: StartScreenState.()-> StartScreenState)
    {
        _uiState.update { currentState ->
            func(currentState)
        }
    }
}