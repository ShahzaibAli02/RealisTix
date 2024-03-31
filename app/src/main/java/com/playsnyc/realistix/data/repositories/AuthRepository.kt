package com.playsnyc.realistix.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.tasks.await

class AuthRepository(private val sharedPref: SharedPref)
{


    suspend fun signInWithEmailAndPassword(email: String, password: String): Response<Boolean>
    {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        return try
        {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }

    }
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Response<Boolean>
    {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        return try
        {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception)
        {
            Response.Error(e.message ?: "")
        }

    }

}