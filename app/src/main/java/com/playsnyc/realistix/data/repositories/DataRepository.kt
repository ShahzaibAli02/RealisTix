package com.playsnyc.realistix.data.repositories

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.playsnyc.realistix.SHARED_PREF_KEYS
import com.playsnyc.realistix.data.datasource.EventDataSource
import com.playsnyc.realistix.data.model.Connection
import com.playsnyc.realistix.data.model.ConnectionNumber
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.sealed.Response
import com.playsnyc.realistix.utils.toJson
import kotlinx.coroutines.flow.Flow
import java.util.Random

class DataRepository(private val sharedPref: SharedPref, private val firestoreRepo: FireStoreRepository)
{
    private val eventDataSource: EventDataSource by lazy {
        EventDataSource(firestoreRepo)
    }

    suspend fun getUser(uid:String?): Response<User>
    {
        if (uid==null)
            return Response.Error("User id is null")
        runCatching {
            return  firestoreRepo.getUserFromServer(uid)
        }
        return Response.Error("")
    }
    suspend fun updateUser(user:User):Response<out Any>
    {
        return firestoreRepo.updateUser(user)
    }

    suspend fun generateNumber(uid: String?):Response<String>
    {
        if (uid==null)
            return Response.Error("User id is null")
        val connectionNumber=ConnectionNumber(uid=uid, number = Random().nextInt(1000).toString())
        runCatching {
            return  when (val result = firestoreRepo.createNumber(connectionNumber))
            {
                is Response.Error -> Response.Error(result.message)
                is Response.Success -> Response.Success(connectionNumber.number)
            }
        }
        return Response.Error("Unknown Error")
    }




    suspend  fun postEvent(images:List<Uri>, data: Event, onUpdate:(state: ScreenState)->Unit)
    {
        val mUid= FirebaseAuth.getInstance().currentUser?.uid
        if(mUid==null)
        {
            onUpdate(ScreenState.Error("Please Login to continue"))
            return
        }
        val imagesLink= mutableListOf<String>()
        onUpdate(ScreenState.Loading("Uploading Images",progress=0))

        images.forEachIndexed { index, uri ->
            val imageLink=firestoreRepo.uploadFile(uri)

            val progress: Int = ((index + 1).toDouble() / images.size * 100).toInt()
            if(imageLink is Response.Success)
            {
                imagesLink.add(imageLink.data!!)
                onUpdate(ScreenState.Loading("Uploading Images ${index+1}/${images.size}",progress=progress))
            }
            if(imageLink is Response.Error)
            {
                onUpdate(ScreenState.Error("Failed to upload images"))
                return@forEachIndexed
            }


        }
        onUpdate(ScreenState.Loading("Finalizing.."))
        when(val result=firestoreRepo.postEventServer(data.copy(uid=mUid,images=imagesLink)))
        {
            is Response.Error -> onUpdate(ScreenState.Error(result.message))
            is Response.Success ->onUpdate(ScreenState.Success("Done"))
        }

    }




    fun fetchNextPage(pageSize: Int): Flow<PagingData<Event>>
    {
        return Pager(
                config = PagingConfig(pageSize),
                pagingSourceFactory = {
                    eventDataSource
                }
        ).flow
    }

    suspend fun getAllActivitiesForUid(uid:String?):Response<List<Event>>
    {
        if(uid==null) return Response.Error("User id is null")
        return firestoreRepo.getAllActivitiesForUid(uid)
    }
    suspend fun getAllSavedActivitiesForUid(uid:String?):Response<List<Event>>
    {
        if(uid==null) return Response.Error("User id is null")
        return firestoreRepo.getAllSavedActivitiesForUid(uid)
    }
    suspend fun loadAllConnections():Response<List<User>>{
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Response.Error("User id is null")
        return firestoreRepo.loadAllConnections(uid)
    }
    suspend fun loadAllAttande(eventId:String):Response<List<User>>{
        return firestoreRepo.loadAllAttendesForEvent(eventId)
    }
    suspend fun loadConnectionsForDate(date:Long):Response<List<User>>{
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Response.Error("User id is null")
        return firestoreRepo.loadConnectionsForDate(uid,date)
    }
    suspend fun createConnection(number: String): Response<out Any>
    {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Response.Error("User id is null")
        val result=firestoreRepo.getUserIdByNumber(number)
        if(result is Response.Error)
            return result
        val targetUserId= (result as Response.Success).data!!
        if(firestoreRepo.hasInMyContactsList(uid,targetUserId) is Response.Success)
            return Response.Error("Already in contact list")
        if(targetUserId==uid) return Response.Error("Same Id Error")
        return firestoreRepo.createConnection(Connection(uid=uid,targetUid=targetUserId,ids= listOf(uid,targetUserId)))

    }



    suspend fun confirmBooking(docId: String?, uid: String?):Response<Unit>
    {
        if(uid==null)  return Response.Error("User id is null")
        return firestoreRepo.bookEvent(docId!!,uid)

    }

    suspend fun loadAttendList(eventDocID: String):Response<List<String>>
    {
        return firestoreRepo.getAttendList(eventDocID)
    }

   suspend fun getEventDetails(docId: String):Response<Event>
    {
      return firestoreRepo.getEventDetails(docId)
    }

    suspend fun deleteConnection(uid:String?,targetUid:String):Response<out Any>
    {
        if(uid==null)return Response.Error("User Id Null")
        return firestoreRepo.deleteConnection(uid,targetUid)
    }
   suspend fun deleteEvent(docId: String):Response<out Any>
    {
        return firestoreRepo.deleteEvent(docId)
    }

}