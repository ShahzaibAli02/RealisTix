package com.playsnyc.realistix.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson


fun <T:Any> DocumentSnapshot.toCustomObject(klass: Class<T>):T?{
    return  Gson().fromJson(this.data?.toMap()?.mapToJson(), klass)
}

// Extension function to convert Map to JSON
fun Map<*, *>?.mapToJson(): String {
    return Gson().toJson(this)
}
fun Any.toJson(): String
{
    kotlin.runCatching {
        return  Gson().toJson(this)
    }
    return ""
}