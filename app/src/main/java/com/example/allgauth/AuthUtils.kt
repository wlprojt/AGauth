package com.example.allgauth

import android.content.Context

fun isLoggedIn(context: Context): Boolean {
    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    return prefs.getString("jwt", null) != null
}
