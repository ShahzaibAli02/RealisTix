package com.playsnyc.realistix.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object MyUtils
{
    fun openUrl(context:Context,Url: String) {
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }
}