package com.example.allgauth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.allgauth.AuthViewModel

@Composable
fun DashboardScreen(vm: AuthViewModel, onLogout: () -> Unit) {
    val user by vm.user.collectAsState()
    val loading by vm.loading.collectAsState()

    LaunchedEffect(Unit) { vm.loadMe() }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            loading -> CircularProgressIndicator()
            user == null -> Text("No token found / Unauthorized")
            else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome, ${user!!.name}")
                Text(user!!.email)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.logout(); onLogout() }) {
                    Text("Logout")
                }
            }
        }
    }
}
