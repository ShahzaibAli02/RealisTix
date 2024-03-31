package com.playsnyc.realistix.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.log
import com.google.firebase.Timestamp
import com.playsnyc.realistix.Constants
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.sealed.Response

class EventDataSource(val fireStoreRepository: FireStoreRepository): PagingSource<Int, Event>() {
    private val pageSize =Constants.EVENTS_PAGE_SIZE
    var lastEventDateCreated:Timestamp?=null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        try {
            val nextPageNumber = params.key ?: 1 // Initial page number is 1
            val data = fetchDataFromApi(nextPageNumber)
            lastEventDateCreated=data.lastOrNull()?.event_created_date
            return LoadResult.Page(
                data = data,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (data.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            // Handle loading errors
            return LoadResult.Error(e)
        }
    }

    private suspend fun fetchDataFromApi(pageNumber: Int): List<Event> {
        Log.d(
                "Paging",
                "fetchDataFromApi pageNumber ${pageNumber} "
        )
        if(pageNumber==1) lastEventDateCreated=null
        return when(val result=fireStoreRepository.getPaginatedEvents(pageNumber,pageSize,lastEventDateCreated))
        {
            is Response.Error -> {
                Log.i("EventDataSource",result.message)
                emptyList()
            }
            is Response.Success -> {
                result.data!!
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        Log.d(
                "Paging",
                "getRefreshKey() "
        )
        // Return the key representing the initial state or page number
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}