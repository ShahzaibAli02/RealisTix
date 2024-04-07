package com.playsnyc.realistix.utils

import java.text.SimpleDateFormat
import java.util.Calendar
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


    fun getCurrentDate(): Long {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)
        return currentDate.timeInMillis
    }

    fun getCurrentTime(): Long {
        val currentTime = Calendar.getInstance()
        currentTime.set(Calendar.YEAR, 0)
        currentTime.set(Calendar.MONTH, 0)
        currentTime.set(Calendar.DAY_OF_MONTH, 0)
        return currentTime.timeInMillis
    }



}