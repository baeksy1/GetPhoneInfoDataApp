package com.crepass.crepassmessage.utils

import android.content.Context
import android.content.SharedPreferences

class GetPerference {
    private lateinit var preferences: SharedPreferences
    fun getToken(context: Context): String {
        preferences = context.getSharedPreferences("keyword", Context.MODE_PRIVATE)

        return preferences.getString("token", "") ?: "token is null"
    }
}