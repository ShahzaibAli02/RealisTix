package com.playsnyc.realistix.ui.screens.myactivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.UIState
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MyActivitiesScreenViewModel(private val dataRepository: DataRepository):ViewModel()
{

    private val _uiState = MutableStateFlow(UIState<Unit>())
    val uiState: StateFlow<UIState<Unit>> = _uiState.asStateFlow()

    val upcomingActivitiesList=MutableStateFlow(emptyList<Event>())
    val pastActivitiesList=MutableStateFlow(emptyList<Event>())
    fun updateUiState(func: UIState<Unit>.() -> Unit)
    {
        _uiState.update { it.copy().apply(func) }
    }
    fun splitIntoUpcomingAndPast(list:List<Event>): Pair<List<Event>, List<Event>>
    {
        val upcomingList= mutableListOf<Event>()
        val pastList= mutableListOf<Event>()
        val today= Date().time
        list.forEach {
            if(mergedDateTime(it.date,it.startTime)>today)
                upcomingList.add(it)
            else
                pastList.add(it)
        }
        return Pair(upcomingList,pastList)
    }

    private fun mergedDateTime(date: Long, startTime: Long): Long {
        // Extract the time components (hours, minutes, seconds) from the startTime
        val timeCalendar = Calendar.getInstance().apply {
            timeInMillis = startTime
        }
        val hourOfDay = timeCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = timeCalendar.get(Calendar.MINUTE)
        val second = timeCalendar.get(Calendar.SECOND)

        // Set the time components to the date
        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
            set(Calendar.MILLISECOND, 0) // Clear milliseconds for precision
        }

        // Return the merged date and time as milliseconds
        return dateCalendar.timeInMillis
    }

    fun loadActivities(uid:String?)=viewModelScope.launch {
        updateUiState{state=ScreenState.Loading()}
        parseResult(dataRepository.getAllActivitiesForUid(uid))
    }
    fun loadSavedActivities(uid:String?)=viewModelScope.launch {
        updateUiState{state=ScreenState.Loading()}
        parseResult(dataRepository.getAllSavedActivitiesForUid(uid))
    }
    fun parseResult(result: Response<List<Event>>){
        updateUiState{state=ScreenState.Loading()}
        when(result)
        {
            is Response.Error -> {
                updateUiState{state=ScreenState.Error(result.message)}
            }
            is Response.Success ->{
                updateUiState{state=ScreenState.None()}
                val splitIntoUpcomingAndPast = splitIntoUpcomingAndPast(result.data!!)
                upcomingActivitiesList.value=splitIntoUpcomingAndPast.first
                pastActivitiesList.value=splitIntoUpcomingAndPast.second
            }
        }
    }




}
