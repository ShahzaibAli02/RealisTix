package com.playsnyc.realistix.model

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}