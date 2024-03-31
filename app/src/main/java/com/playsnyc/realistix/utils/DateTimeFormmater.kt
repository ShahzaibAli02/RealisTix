package com.playsnyc.realistix.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeFormmater
{


    fun formateDate(date: Long, pattern: String = "dd MMMM (EEE)"): String
    {
        return formateDate(Date(date),pattern)
    }
    fun formateTime(date: Long, pattern: String = "HH:mm"): String
    {
        return formateDate(Date(date),pattern)
    }
    fun formateTime(date: Date, pattern: String = "HH:mm"): String
    {
        return formateDate(date,pattern)
    }
    fun formateDate(date: Date, pattern: String = "dd MMMM (EEE)"): String
    {
        runCatching {
            return SimpleDateFormat(
                    pattern,
                    Locale.ENGLISH
            ).format(date)
        }
        return ""
    }



}