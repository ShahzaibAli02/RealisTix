package com.playsnyc.realistix.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.model.Error
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

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    val _email = MutableStateFlow("")

    val _password = MutableStateFlow("")


    fun update(func: UIState.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }

    fun login() = viewModelScope.launch {

        if (_email.value.isBlank() || _password.value.isBlank())
        {
            update { isLoading = false; error = Error("Fill all required fields") }
            return@launch
        }
        update { isLoading = true; error = null }
        when (val result = authRepository.signInWithEmailAndPassword(
                _email.value,
                _password.value
        ))
        {
            is Response.Error -> update { isLoading = false;error = Error(result.message) }
            is Response.Success ->
            {
                update { isLoading = false;error = null;uiScreen = UiScreens.DASHBOARD }
            }
        }
    }

}