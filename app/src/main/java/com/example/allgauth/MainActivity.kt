package com.example.allgauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allgauth.api.ApiClient
import com.example.allgauth.ui.LoginScreen
import com.example.allgauth.ui.MainNavGraph
import com.example.allgauth.ui.theme.AllgauthTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = ApiClient.create("http://192.168.29.139:3000/")
        setContent {
            AllgauthTheme {
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(application, api)
                )

                MainNavGraph(authViewModel = authViewModel)
            }
        }
    }
}
