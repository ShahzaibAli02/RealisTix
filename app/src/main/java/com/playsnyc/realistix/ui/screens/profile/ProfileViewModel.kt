package com.playsnyc.realistix.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.ProfileOption
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.SocialMediaItem
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.sealed.Response
import com.playsnyc.realistix.ui.screens.auth.StartScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(val dataRepository: DataRepository) : ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<User>())
    val uiState: StateFlow<UIState<User>> = _uiState.asStateFlow()
    fun updateUiState(func: UIState<User>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }
    val _options = listOf(
            ProfileOption(
                    id = R.id.createEvents,
                    title = "Create Events",
                    image = R.drawable.baseline_share_24
            ),
            ProfileOption(
                    id = R.id.updateAccount,
                    title = "Edit Social Media Accounts",
                    image = R.drawable.baseline_switch_account_24
            ),
            ProfileOption(
                    id = R.id.your_activities,
                    title = "Your Activities",
                    image = R.drawable.outline_check_circle_24
            ),
            ProfileOption(
                    id = R.id.savedEvents,
                    title = "Saved Events",
                    image = R.drawable.baseline_bookmark_border_24
            ),
            ProfileOption(
                    id = R.id.termsConditions,
                    title = "Terms & Conditions",
                    image = R.drawable.baseline_feed_24
            ),
            ProfileOption(
                    id = R.id.privacy_policy,
                    title = "Privacy Policy",
                    image = R.drawable.baseline_privacy_tip_24
            ),
            ProfileOption(
                    id = R.id.log_out,
                    title = "Log Out",
                    image = R.drawable.baseline_logout_24
            ),
    )


    fun updateSocialMediaList(list:List<SocialMediaItem>,onSuccess:()->Unit)=viewModelScope.launch{
        uiState.value.data?.let {
            updateUiState { state = ScreenState.Loading() }
            it.socialMedia=list
            when (val result = dataRepository.updateUser(it))
            {
                is Response.Error ->
                {
                    updateUiState {
                        state = ScreenState.Error(result.message)
                    }
                }

                is Response.Success ->
                {
                    onSuccess()
                    updateUiState {
                        state = ScreenState.None();
                    }
                }
            }
        }
    }
    fun loadUser() = viewModelScope.launch {
        updateUiState { state = ScreenState.Loading() }
        when (val result = dataRepository.getUser(FirebaseAuth.getInstance().uid))
        {
            is Response.Error ->
            {
                updateUiState {
                    state = ScreenState.Error(result.message)
                }
            }

            is Response.Success ->
            {
                updateUiState {
                    state = ScreenState.None();data = result.data
                }
            }
        }

    }

}