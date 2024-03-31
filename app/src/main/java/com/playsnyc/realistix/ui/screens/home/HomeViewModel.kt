package com.playsnyc.realistix.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.Timestamp
import com.playsnyc.realistix.Constants
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.repositories.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(val dataRepository: DataRepository):ViewModel()
{
    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()


    var currentEvent:Event? = null
    private val _eventsList = MutableStateFlow<MutableList<Event>>(mutableListOf())
    val eventsList: StateFlow<List<Event>> = _eventsList.asStateFlow()

//    fun loadEvents(fromServer:Boolean=false)=viewModelScope.launch {
//        updateUiState { state=ScreenState.Loading() }
//        dataRepository.
//
//    }

    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }



    private val _pagedList: MutableStateFlow<PagingData<Event>> = MutableStateFlow(PagingData.empty())
    val pagedList: StateFlow<PagingData<Event>> = _pagedList

    init {
        // Load the initial page of data when ViewModel is initialized
        loadNextPage()
    }

    fun loadNextPage() {
        // Fetch the next page of data from the repository
        viewModelScope.launch {
            updateUiState{state=ScreenState.Loading()}

            dataRepository.fetchNextPage(Constants.EVENTS_PAGE_SIZE).cachedIn(viewModelScope).collect {
                _pagedList.value = it
                updateUiState{state=ScreenState.None()}
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