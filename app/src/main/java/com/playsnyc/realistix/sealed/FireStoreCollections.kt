package com.playsnyc.realistix.sealed

sealed class FireStoreCollections(val name:String){
    object Users:FireStoreCollections("users")
    object Numbers:FireStoreCollections("numbers")
    object Connections:FireStoreCollections("connections")
    object Events:FireStoreCollections("events")
}