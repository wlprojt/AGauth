package com.example.allgauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.allgauth.AuthViewModel
import com.example.allgauth.api.AuthApi
import com.example.allgauth.api.GoogleLoginRequest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(vm: AuthViewModel, api: AuthApi, onLoggedIn: () -> Unit) {
    val scope = rememberCoroutineScope()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        GoogleSignInButton(scope = scope) { idToken ->
            vm.viewModelScope.launch {
                try {
                    val res = api.googleLogin(GoogleLoginRequest(idToken))
                    val jwt = res.token ?: throw Exception("No JWT")

                    vm.saveJwt(jwt)

                    // ✅ Navigate ONLY after token is saved
                    onLoggedIn()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        if (loading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        if (error != null) {
            Spacer(Modifier.height(16.dp))
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
