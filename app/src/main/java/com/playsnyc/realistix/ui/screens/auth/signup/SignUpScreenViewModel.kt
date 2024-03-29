package com.playsnyc.realistix.ui.screens.auth.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.model.Error
import com.playsnyc.realistix.model.ScreenState
import com.playsnyc.realistix.model.UIState
import com.playsnyc.realistix.model.User
import com.playsnyc.realistix.repositories.AuthRepository
import com.playsnyc.realistix.repositories.FireStoreRepository
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    val fireStoreRepository: FireStoreRepository,
    val authRepository: AuthRepository,
) : ViewModel()
{

    private val _userState = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()


    private val _currentScreen = MutableStateFlow(UiScreens.SIGNUP_SCREEN1)
    val currentScreen: StateFlow<UiScreens> = _currentScreen

    fun changeScreen(whichScreen: UiScreens)=viewModelScope.launch {

        updateUiState{state = ScreenState.Loading()}

        if(whichScreen == UiScreens.SIGNUP_SCREEN2)
        {
            val result= isValidDataForScreen1()
            if(result is Response.Error)
            {
                updateUiState {state = ScreenState. Error(result.message) }
                return@launch
            }

        }
        if (whichScreen == UiScreens.SIGNUP_SCREEN3 && !isValidDataForScreen2())
        {
            updateUiState { state = ScreenState.Error("Please fill all required fields") }
            return@launch
        }
        updateUiState{state = ScreenState.None()}
        _currentScreen.value = whichScreen
    }
    private suspend fun isValidDataForScreen1(): Response<Boolean>
    {

        if (userState.value.name!!.isBlank() ||  userState.value.email!!.isBlank() || userState.value.password!!.length<6)
            return Response.Error("Please fill all fields")
        val result=  checkIfEmailExists(userState.value.email!!)
        if(result is Response.Success && result.data==true)
        {
            return Response.Error("Email already exist")
        }
        return result
    }

    private fun isValidDataForScreen2(): Boolean
    {

        return userState.value.let { (it.name!!.isBlank() || it.occupation!!.isBlank() || it.organization!!.isBlank() || it.nationality!!.isBlank() || it.age == 0).not() }
    }

    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }

    fun updateUser(func: User.() -> Unit)
    {
        _userState.update { it.copy().apply(func) }
    }
    fun signUp()=viewModelScope.launch {
        updateUiState {state = ScreenState.Loading() }
        var result:Response<Boolean> =authRepository.createUserWithEmailAndPassword(userState.value.email!!,userState.value.password!!)
        if(result is Response.Success )
        {
            userState.value.uid=FirebaseAuth.getInstance().currentUser!!.uid
            result=fireStoreRepository.createUser(userState.value)
        }
        if(result is Response.Error)
        {
            updateUiState { state = ScreenState.Error(result.message)}
            return@launch
        }
        updateUiState { state = ScreenState.Loading()}
        changeScreen(UiScreens.DASHBOARD)

    }
    suspend fun checkIfEmailExists(email: String) = fireStoreRepository.emailExists(email.trim().lowercase())


    fun uploadImage(it: Uri) = viewModelScope.launch {
        updateUiState { state = ScreenState.Loading() }
        when (val result = fireStoreRepository.uploadFile(it))
        {
            is Response.Error -> updateUiState { state = ScreenState.Error(result.message) }
            is Response.Success ->
            {
                updateUiState { state = ScreenState.None()}
                updateUser { image = result.data!! }
            }
        }
    }


}