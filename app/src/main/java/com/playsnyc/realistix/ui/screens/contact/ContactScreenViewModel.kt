package com.playsnyc.realistix.ui.screens.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.model.isLoading
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

class ContactScreenViewModel(val dataRepository: DataRepository):ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    val _randomNumber = MutableStateFlow("")
    val _connectionsList = MutableStateFlow(listOf<User>())
    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }

    init
    {

        viewModelScope.launch {
            while (isActive)
            {
                loadConnectionsOfToday()
                delay(5*1000L)
            }

        }
    }
    fun generateNumber()=viewModelScope.launch {
        updateUiState { state=ScreenState.Loading()}
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        when (val result=dataRepository.generateNumber(uid))
        {

            is Response.Error ->
            {
                _randomNumber.value = "Error"
                updateUiState { state = ScreenState.Error("Failed to generate number") }
                return@launch
            }

            is Response.Success ->
            {
                _randomNumber.value = result.data!!
                updateUiState { state = ScreenState.Success("Failed to generate number") }
            }
        }
        updateUiState { state=ScreenState.None()}
    }

    fun loadConnectionsOfToday() = viewModelScope.launch {
        updateUiState { state=ScreenState.Loading()}
        when (val result = dataRepository.loadConnectionsForDate(DateTimeFormmater.getCurrentDate()))
        {
            is Response.Error ->{
                updateUiState { state = ScreenState.Error(result.message) }
            }
            is Response.Success -> {
                _connectionsList.value=result.data!!
                updateUiState { state = ScreenState.None() }
            }
        }
    }
    fun connectWithUser(number: String)=viewModelScope.launch {
        updateUiState { state=ScreenState.Loading()}
        when (val result = dataRepository.createConnection(number))
        {
            is Response.Error ->{
                updateUiState { state = ScreenState.Error(result.message) }
            }
            is Response.Success -> {
                updateUiState { state = ScreenState.Success("Connection Added") }
            }
        }
    }

    //    fun updateDataState(func: Unit.() -> Unit)
//    {
//        _uiState.update {
//            it.copy().apply {
//                data = this.data!!.copy().apply(func)
//            }
//        }
//    }

}