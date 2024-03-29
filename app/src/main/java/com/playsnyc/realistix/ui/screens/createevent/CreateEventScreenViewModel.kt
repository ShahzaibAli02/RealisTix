package com.playsnyc.realistix.ui.screens.createevent

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.model.Event
import com.playsnyc.realistix.model.ScreenState
import com.playsnyc.realistix.model.UIState
import com.playsnyc.realistix.repositories.DataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventScreenViewModel(val dataRepository: DataRepository):ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Event>(data = Event()))
    val uiState: StateFlow<UIState<Event>> = _uiState.asStateFlow()


    val selectedImages = MutableStateFlow( mutableStateListOf<Uri>() )

    var job: Job? = null
    fun updateUiState(func: UIState<Event>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }
    fun updateDataState(func: Event.() -> Unit)
    {
        _uiState.update { it.copy().apply {
            data=this.data!!.copy().apply(func)
        }}
    }

    fun validateFirstPage(): Boolean
    {
        val data=uiState.value.data!!
        return !(data.name.isBlank() || data.organizer.isBlank() || data.type.isBlank() || data.description.isBlank() || data.address.isBlank())
    }

    fun cancelUpload()
    {

        job?.cancel()
        updateUiState{
            state=ScreenState.None()
        }
    }
    fun postEvent(){
        job=viewModelScope.launch {
            dataRepository.postEvent(selectedImages.value,uiState.value.data!!){
                updateUiState {
                    state=it
                }
            }

        }
    }

    fun validateSecondPage(): Boolean
    {
        val data=uiState.value.data!!
        var errorMessage=""
        if(selectedImages.value.isEmpty())
        {
            errorMessage="Images are required"
        }
        else
        if(data.capacity==0)
        {
            errorMessage="Enter correct event capacity!!"
        }

        if(errorMessage.isNotBlank())
        {
            updateUiState {
                state=ScreenState.Error(errorMessage)
            }
        }

        return errorMessage.isBlank()

    }

    val _eventTypes= listOf("Conference"
            ,"Festival / Fair"
            ,"Networking Event"
            ,"Social Gathering"
            ,"Seminar / Talk"
            ,"Tradeshow / Expo"
            ,"Workshop / Class"
            ,"Other"
    )

}