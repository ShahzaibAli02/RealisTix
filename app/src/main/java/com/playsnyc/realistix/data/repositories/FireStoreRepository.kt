package com.playsnyc.realistix.data.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.playsnyc.realistix.SHARED_PREF_KEYS
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.sealed.FireStoreCollections
import com.playsnyc.realistix.sealed.Response
import com.playsnyc.realistix.utils.toCustomObject
import com.playsnyc.realistix.utils.toJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class FireStoreRepository()
{
    data class FireBaseData(val docId: String, val data: Any)


    suspend fun getUserFromServer(uid:String):Response<User>
    {
        val db = Firebase.firestore
        return try
        {
            val result = db.collection(FireStoreCollections.Users.name).document(uid).get().await()
            result.toCustomObject(User::class.java)?.let {
                Response.Success(it)
            }?:run {
                Response.Error("User not found")
            }
        }
        catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }
    }
    suspend fun emailExists(email: String): Response<Boolean>
    {
        val db = Firebase.firestore
        return try
        {
            val result = db.collection(FireStoreCollections.Users.name).whereEqualTo("email",email).get().await()
            Response.Success(result.isEmpty.not())
        }
        catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }

    }
    suspend fun uploadFile(file:Uri):Response<String>
    {

        return try
        {
            val storageRef = Firebase.storage.reference
            val imagesRef = storageRef.child("images/${file.lastPathSegment}")
             imagesRef.putFile(file).await()
            return  Response.Success(imagesRef.downloadUrl.await().toString())
        }
        catch (e:Exception)
        {
            Response.Error(e.message ?: "")
        }

    }

    suspend fun createUser(value: User):Response<Boolean>
    {
        return postData(FireStoreCollections.Users.name){
            FireBaseData(it,value.copy(password = ""))
        }
    }



    suspend fun getPaginatedEvents(pageNumber:Int,pageSize:Int,lastEventDateCreated:Timestamp?):Response<List<Event>>
    {
        val db = Firebase.firestore
        return try
        {

            Log.d(
                    "TAG",
                    "getPaginatedEvents: lastEventDateCreated = ${lastEventDateCreated}"
            )


            val querySnapshot = db.collection(FireStoreCollections.Events.name)
                .orderBy("event_created_date",Query.Direction.DESCENDING).let {
                    if (lastEventDateCreated!=null)
                        it.startAfter(lastEventDateCreated)
                    else it
                }
                .limit(pageSize.toLong()).get().await()

            val eventsList = mutableListOf<Event>()

            querySnapshot.documents.forEach {
                it.toCustomObject(Event::class.java)?.let { eventsList.add(it) }
            }
            Log.d(
                    "TAG",
                    "getPaginatedEvents:EVENTS ${eventsList} "
            )
            Response.Success(eventsList)
        }
        catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }
    }

    suspend fun postData(collectionName:String,onDataGet:(docId:String)-> FireBaseData):Response<Boolean>
    {
        val db = Firebase.firestore
        return try
        {
            val collection=db.collection(collectionName)
            val docID=collection.document().id
            val firebaseData=onDataGet(docID)
            collection.document(firebaseData.docId).set(firebaseData.data).await()
            Response.Success(true)
        }
        catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }
    }
    suspend fun postEventServer(event: Event): Response<Boolean>
    {
      return postData (FireStoreCollections.Events.name){docId->
          FireBaseData(docId,event.copy(docId = docId))
       }
    }
}
