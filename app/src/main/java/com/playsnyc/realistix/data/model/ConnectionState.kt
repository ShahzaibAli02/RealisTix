package com.playsnyc.realistix.data.model

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}