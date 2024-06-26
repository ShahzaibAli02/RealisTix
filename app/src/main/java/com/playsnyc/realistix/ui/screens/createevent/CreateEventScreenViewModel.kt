package com.playsnyc.realistix.ui.screens.createevent

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.sealed.Response
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
    fun clearDataState()
    {
        _uiState.update { it.copy().apply {
            data=Event()
        }}
    }

    fun validateFirstPage(): Boolean
    {
        val data=uiState.value.data!!
        return !(data.name.isBlank() || data.organizer.isBlank() || data.type.isBlank() || data.description.isBlank() || (data.locationType!="To be announced" && data.address.isBlank()))
    }

    fun loadEventDetails(docId:String)=viewModelScope.launch {
        updateUiState { state=ScreenState.Loading() }
        when(val result=dataRepository.getEventDetails(docId))
        {
            is Response.Error -> {
                updateUiState{ScreenState.Error(result.message)}
            }
            is Response.Success -> {
                updateUiState{state=ScreenState.None()}
                _uiState.update { it.copy().apply {
                    data=result.data
                }}
            }
        }
    }
    fun cancelUpload()
    {

        job?.cancel()
        updateUiState{
            state= ScreenState.None()
        }
    }
    fun deleteEvent(docId: String,onDeleted:()->Unit)=viewModelScope.launch {
        updateUiState{state=ScreenState.Loading()}
        when(val result=dataRepository.deleteEvent(docId))
        {
            is Response.Error ->{
                updateUiState { state=ScreenState.Error("Failed to delete") }
            }
            is Response.Success -> {
                updateUiState { state=ScreenState.Success("Event Deleted") }
                clearDataState()
                onDeleted()
            }
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
                state= ScreenState.Error(errorMessage)
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