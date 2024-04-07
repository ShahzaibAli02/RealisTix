package com.playsnyc.realistix.ui.screens.myconnections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.sealed.Response
import com.playsnyc.realistix.utils.DateTimeFormmater
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MyConnectionScreenViewModel(private val dataRepository: DataRepository):ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    val _randomNumber = MutableStateFlow("")
    val _connectionsList = MutableStateFlow(mutableListOf<User>())
    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }


    fun loadAllConnections() = viewModelScope.launch {
        updateUiState { state=ScreenState.Loading()}
        when (val result = dataRepository.loadAllConnections())
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
    fun removeUser(targetUid: String)=viewModelScope.launch {
        val myUid=FirebaseAuth.getInstance().currentUser?.uid
        updateUiState { state=ScreenState.Loading()}
        when (val result = dataRepository.deleteConnection(myUid,targetUid))
        {
            is Response.Error ->{
                updateUiState { state = ScreenState.Error(result.message) }
            }
            is Response.Success -> {
                updateUiState { state = ScreenState.Success("Connection Removed") }
                _connectionsList.update {list->
                    list.find { it.uid==targetUid }?.let { list.remove(it) }
                    list
                }
            }
        }
    }

}