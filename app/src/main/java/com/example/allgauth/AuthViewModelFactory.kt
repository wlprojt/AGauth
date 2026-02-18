package com.example.allgauth


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.allgauth.api.AuthApi

class AuthViewModelFactory(
    private val application: Application,
    private val api: AuthApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application, api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
