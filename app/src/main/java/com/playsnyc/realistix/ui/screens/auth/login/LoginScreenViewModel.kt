package com.playsnyc.realistix.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.model.Error
import com.playsnyc.realistix.model.ScreenState
import com.playsnyc.realistix.model.UIState
import com.playsnyc.realistix.repositories.AuthRepository
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(val authRepository: AuthRepository) : ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    val _email = MutableStateFlow("")

    val _password = MutableStateFlow("")


    fun update(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }

    fun login() = viewModelScope.launch {

        if (_email.value.isBlank() || _password.value.isBlank())
        {
            update {  state = ScreenState.Error("Fill all required fields") }
            return@launch
        }
        update { state = ScreenState.Loading() }
        when (val result = authRepository.signInWithEmailAndPassword(
                _email.value,
                _password.value
        ))
        {
            is Response.Error -> update { state = ScreenState.Error(result.message) }
            is Response.Success ->
            {
                update { state = ScreenState.None();uiScreen = UiScreens.DASHBOARD }
            }
        }
    }

}