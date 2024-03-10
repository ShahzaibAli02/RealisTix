package com.playsnyc.realistix.repositories

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.playsnyc.realistix.model.User
import com.playsnyc.realistix.sealed.FireStoreCollections
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.tasks.await

class FireStoreRepository
{


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
        val db = Firebase.firestore
        return try
        {
            db.collection(FireStoreCollections.Users.name).document(value.uid).set(value.copy(password = "")).await()
            Response.Success(true)
        }
        catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }
    }
}
