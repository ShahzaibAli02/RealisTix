package com.playsnyc.realistix.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver(private val onReceive: (Intent?)->Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        onReceive.invoke(intent)
    }
}
