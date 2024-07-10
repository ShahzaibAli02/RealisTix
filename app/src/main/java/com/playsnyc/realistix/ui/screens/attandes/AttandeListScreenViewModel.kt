package com.playsnyc.realistix.ui.screens.attandes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttandeListScreenViewModel(private val dataRepository: DataRepository):ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    val _connectionsList = MutableStateFlow(mutableListOf<User>())
    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }


    fun loadAllAttandee(eventID: String) = viewModelScope.launch {
        updateUiState { state=ScreenState.Loading()}
        when (val result = dataRepository.loadAllAttande(eventID))
        {
            is Response.Error ->{
                updateUiState { state = ScreenState.Error(result.message) }
            }
            is Response.Success -> {
                _connectionsList.value=result.data!!.toMutableList()
                updateUiState { state = ScreenState.None() }
            }
        }
    }

}