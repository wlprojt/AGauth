package com.example.allgauth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.allgauth.api.AuthApi
import com.example.allgauth.api.TokenStore
import com.example.allgauth.api.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    app: Application,
    private val api: AuthApi
) : AndroidViewModel(app) {

    private val store = TokenStore(app)

    private val _user = MutableStateFlow<UserDto?>(null)
    val user: StateFlow<UserDto?> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun saveJwt(token: String) {
        store.save(token)
    }

    fun loadMe() {
        val token = store.get() ?: run {
            _error.value = "No token found"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            try {
                val res = api.me("Bearer $token")
                _user.value = res.user
            } catch (e: Exception) {
                _error.value = "Unauthorized"
                store.clear()
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        store.clear()
        _user.value = null
    }
}
