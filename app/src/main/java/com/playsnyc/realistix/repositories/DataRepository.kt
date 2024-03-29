package com.playsnyc.realistix.repositories

import android.net.Uri
import androidx.compose.ui.util.fastForEachIndexed
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.playsnyc.realistix.SHARED_PREF_KEYS
import com.playsnyc.realistix.model.Event
import com.playsnyc.realistix.model.ScreenState
import com.playsnyc.realistix.model.User
import com.playsnyc.realistix.sealed.Response
import kotlinx.coroutines.yield

class DataRepository(private val sharedPref: SharedPref,val firestoreRepo:FireStoreRepository)
{

    suspend fun getUser(uid:String?): Response<User>
    {
        if (uid==null)
            return Response.Error("User id is null")
        runCatching {
            sharedPref.getString(SHARED_PREF_KEYS.USER_DATA)?.let { userJson->
                if(userJson.isBlank().not())
                {
                    val user= Gson().fromJson(userJson,
                            User::class.java)
                    if(user!=null && user.uid==uid)
                        return Response.Success(user)
                }

            }
            return  firestoreRepo.getUserFromServer(uid)
        }
        return Response.Error("")
    }

    suspend  fun postEvent(images:List<Uri>,data: Event,onUpdate:(state: ScreenState)->Unit)
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

            val progress=(index+1)/images.size
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

}