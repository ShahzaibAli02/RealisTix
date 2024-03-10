package com.playsnyc.realistix.sealed

sealed class Response<T>(val data:T?)
{
    class Error<T>(val message:String=""):Response<T>(null)
    class Success<T>(data:T):Response<T>(data)
}