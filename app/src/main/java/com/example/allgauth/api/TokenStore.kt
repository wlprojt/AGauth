package com.example.allgauth.api

import android.content.Context

class TokenStore(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun save(token: String) = prefs.edit().putString("jwt", token).apply()
    fun get(): String? = prefs.getString("jwt", null)
    fun clear() = prefs.edit().remove("jwt").apply()
}
