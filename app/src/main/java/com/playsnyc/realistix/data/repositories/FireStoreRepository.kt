package com.playsnyc.realistix.data.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.playsnyc.realistix.SHARED_PREF_KEYS
import com.playsnyc.realistix.data.model.Connection
import com.playsnyc.realistix.data.model.ConnectionNumber
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.sealed.FireStoreCollections
import com.playsnyc.realistix.sealed.Response
import com.playsnyc.realistix.utils.toCustomObject
import com.playsnyc.realistix.utils.toJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FireStoreRepository()
{
    data class FireBaseData(val docId: String, val data: Any)


    suspend fun getUserFromServer(uid:String):Response<User>
    {
        val db = Firebase.firestore
        return try
        {
            val result = db.collection(FireStoreCollections.Users.name).whereEqualTo("uid",uid).get().await()
            result.firstOrNull()?.toCustomObject(User::class.java)?.let {
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


    suspend fun createNumber(value:ConnectionNumber):Response<Boolean>
    {
        return postData(FireStoreCollections.Numbers.name){
            FireBaseData(docId = value.uid,value)
        }
    }

    suspend fun createConnection(value:Connection):Response<Boolean>
    {
        return postData(FireStoreCollections.Connections.name){
            FireBaseData(docId = it,value)
        }
    }

    suspend fun getUserIdByNumber(number:String):Response<String>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Numbers.name)
                .whereEqualTo("number",number).get().await()
            val connectionNumber= result.firstOrNull()?.toCustomObject(ConnectionNumber::class.java)
                ?: return Response.Error("User not found")
            return Response.Success(connectionNumber.uid)
        }
        catch (e:Exception)
        {
            return Response.Error(e.localizedMessage?:"")
        }
    }
    suspend fun hasInMyContactsList(myUid:String,targetUid:String):Response<String>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Connections.name)
                .whereArrayContains("ids", myUid).get().await()

            result.forEach {doc->
                doc.toCustomObject(Connection::class.java)
                    ?.let {
                        if(it.uid==targetUid || it.targetUid==targetUid)
                            return Response.Success(doc.id)
                    }
            }
            return Response.Error("Not found")
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            return Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun deleteConnection(uid:String,targetUid: String):Response<out Any>
    {

        val result=hasInMyContactsList(uid,targetUid)
        if(result is Response.Error)
            return result

        val db = Firebase.firestore
        return try
        {
            db.collection(FireStoreCollections.Connections.name).document(result.data!!).delete()
            Response.Success("Deleted")
        } catch (e:Exception)
        {
            Response.Error(e.localizedMessage?:"")
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
      return postData (FireStoreCollections.Events.name){newDocId->
          val docID= event.docId.ifBlank { newDocId }
          FireBaseData(docID,event.copy(docId = docID))
       }
    }

    suspend fun loadConnectionsForDate(uid:String,date:Long):Response<List<User>>
    {
        val db = Firebase.firestore
        try
        {

            val list= mutableListOf<User>()
            val result=db.collection(FireStoreCollections.Connections.name)
                .whereArrayContains("ids", uid)
                .whereGreaterThanOrEqualTo("connectionDateTime",date)
                .whereLessThan("connectionDateTime",(date+ TimeUnit.DAYS.toMillis(1)))
                .get().await()

            result.forEach {
                val connection=it.toCustomObject(Connection::class.java)
                connection?.let {
                    val targetUserId=if(uid==it.uid) it.targetUid else it.uid
                    val user=getUserFromServer(targetUserId)
                    if (user is Response.Success)
                        list.add(user.data!!)

                }
            }
            return Response.Success(list)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            return Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun loadAllConnections(uid:String):Response<List<User>>
    {
        val db = Firebase.firestore
        try
        {
            val list= mutableListOf<User>()
            val result=db.collection(FireStoreCollections.Connections.name)
                .whereArrayContains("ids", uid)
                .get().await()
            result.forEach {
                val connection=it.toCustomObject(Connection::class.java)
                connection?.let {
                    val targetUserId=if(uid==it.uid) it.targetUid else it.uid
                    val user=getUserFromServer(targetUserId)
                    if (user is Response.Success)
                    {
                        list.add(user.data!!)
                    }

                }
            }
            return Response.Success(list)
        }
        catch (e:Exception)
        {
            return Response.Error(e.localizedMessage?:"")
        }
    }




    suspend fun loadAllAttendesForEvent(eventID:String):Response<List<User>>
    {
        val db = Firebase.firestore
        try
        {
            val list= mutableListOf<User>()
            val result=db.collection(FireStoreCollections.Events.name)
                .whereEqualTo("docId", eventID)
                .get().await().first()

            val event=result.toCustomObject(Event::class.java)
            event?.attandesUid?.forEach {
                val user=getUserFromServer(it)
                if (user is Response.Success)
                {
                    list.add(user.data!!)
                }
            }

            return Response.Success(list)
        }
        catch (e:Exception)
        {
            return Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun bookEvent(docId: String, uid: String):Response<Unit>
    {
        val db = Firebase.firestore
        return try
        {
            db.collection(FireStoreCollections.Events.name).document(docId).update("attandesUid", FieldValue.arrayUnion(uid))
                .await()
            Response.Success(Unit)
        } catch (e:Exception)
        {
            Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun getAttendList(eventDocID: String): Response<List<String>>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Events.name).document(eventDocID)
                .get().await()
            val event=result.toCustomObject(Event::class.java)?:return Response.Error("Error reading database")
            return Response.Success(event.attandesUid)
        }
        catch (e:Exception)
        {

            return Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun getEventDetails(docId: String): Response<Event>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Events.name).document(docId)
                .get().await()
            val event=result.toCustomObject(Event::class.java)?:return Response.Error("Error reading database")
            return Response.Success(event)
        }
        catch (e:Exception)
        {

            return Response.Error(e.localizedMessage?:"")
        }
    }



    suspend fun getAllActivitiesForUid(uid:String):Response<List<Event>>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Events.name).whereEqualTo("uid",uid).get().await()
            val list=result.map {
                it.toCustomObject(Event::class.java)!!
            }
            return Response.Success(list)
        }
        catch (e:Exception)
        {

            return Response.Error(e.localizedMessage?:"")
        }
    }
    suspend fun getAllSavedActivitiesForUid(uid:String):Response<List<Event>>
    {
        val db = Firebase.firestore
        try
        {
            val result=db.collection(FireStoreCollections.Events.name).whereArrayContains("attandesUid",uid).get().await()
            val list=result.map {
                it.toCustomObject(Event::class.java)!!
            }
            return Response.Success(list)
        }
        catch (e:Exception)
        {

            return Response.Error(e.localizedMessage?:"")
        }
    }
   suspend fun deleteEvent(docId: String): Response<out Any>
    {

        val db = Firebase.firestore
        return try
        {
            db.collection(FireStoreCollections.Events.name).document(docId).delete().await()
            Response.Success("")
        } catch (e:Exception)
        {

            Response.Error(e.localizedMessage?:"")
        }
    }

    suspend fun updateUser(user: User): Response<out Any>
    {
        val db = Firebase.firestore
        return try
        {
            val document=db.collection(FireStoreCollections.Users.name).whereEqualTo("uid",user.uid).get().await().firstOrNull()
            db.collection(FireStoreCollections.Users.name).document(document!!.id).set(user).await()
            Response.Success("")
        } catch (e:Exception)
        {
            Response.Error(e.localizedMessage?:"")
        }
    }

}
