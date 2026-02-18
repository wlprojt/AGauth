package com.example.allgauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.allgauth.ui.MainScreen
import com.example.allgauth.ui.theme.AllgauthTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var api: AuthApi
    private lateinit var googleSignInClient: GoogleSignInClient

    private var jwtToken by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Setup GoogleSignIn ---
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("135221231784-865kk58lrd3l2e2qnc0onuk1vjhb00t2.apps.googleusercontent.com")
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // --- Setup Retrofit ---
        api = Retrofit.Builder()
            .baseUrl("http://192.168.29.139:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

        setContent {
            AllgauthTheme {
                MainScreen(
                    api = api,
                    onGoogleLogin = { signIn() }, // ✅ pass the lambda
                    jwtToken = jwtToken
                )
            }
        }
    }

    /** Launch Google Sign-In */
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    /** Handle Google Sign-In result */
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (!idToken.isNullOrEmpty()) sendTokenToBackend(idToken)
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    /** Send token to backend and store JWT */
    private fun sendTokenToBackend(idToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.googleLogin(mapOf("idToken" to idToken))
                if (response.isSuccessful) {
                    val body = response.body()
                    val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                    prefs.edit().putString("jwt", body?.token).apply()

                    runOnUiThread { jwtToken = body?.token } // ✅ triggers Compose
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
