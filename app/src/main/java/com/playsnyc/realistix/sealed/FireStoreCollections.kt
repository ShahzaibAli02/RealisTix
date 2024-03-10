package com.playsnyc.realistix.sealed

sealed class FireStoreCollections(val name:String){
    object Users:FireStoreCollections("users")
}