package com.playsnyc.realistix.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPref(val context: Context)
{

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(
            context.packageName,
            MODE_PRIVATE
    )

    fun getString(key: String, default: String = "") = mSharedPreferences.getString(
            key,
            default
    )

    fun getInt(key: String, default: Int) = mSharedPreferences.getInt(
            key,
            default
    )

    fun getBoolean(key: String, default: Boolean) = mSharedPreferences.getBoolean(
            key,
            default
    )

    fun set(key: String, value: String)
    {
        mSharedPreferences.edit().putString(
                key,
                value
        ).apply()
    }

    fun set(key: String, value: Int)
    {
        mSharedPreferences.edit().putInt(
                key,
                value
        ).apply()
    }

    fun set(key: String, value: Boolean)
    {
        mSharedPreferences.edit().putBoolean(
                key,
                value
        ).apply()
    }
}