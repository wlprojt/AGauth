package com.example.allgauth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

// UI states
sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val user: User) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(
    application: Application,
    private val api: AuthApi,
    private val jwtToken: String // ✅ Pass token directly
) : AndroidViewModel(application) {

    var uiState by mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
        private set

    init {
        if (jwtToken.isEmpty()) {
            uiState = DashboardUiState.Error("No token found")
        } else {
            fetchUser()
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            try {
                val response = api.getProfile("Bearer $jwtToken")
                if (response.isSuccessful && response.body() != null) {
                    uiState = DashboardUiState.Success(response.body()!!.user)
                } else {
                    uiState = DashboardUiState.Error("Unauthorized")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = DashboardUiState.Error("Something went wrong")
            }
        }
    }

    fun logout() {
        // Clear stored JWT
        val prefs = getApplication<Application>()
            .getSharedPreferences("auth", Application.MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Update state to force UI refresh
        uiState = DashboardUiState.Error("Logged out")
    }
}
