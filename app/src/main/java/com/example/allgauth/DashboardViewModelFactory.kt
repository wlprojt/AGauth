package com.example.allgauth

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DashboardViewModelFactory(
    private val application: Application,
    private val api: AuthApi,
    private val jwtToken: String // ✅ Pass token
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(application, api, jwtToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
