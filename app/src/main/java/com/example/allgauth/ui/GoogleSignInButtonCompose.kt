package com.example.allgauth.ui

import android.app.Activity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.allgauth.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInButton(
    scope: CoroutineScope,
    onIdToken: suspend (String) -> Unit
) {
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }

    val webClientId = context.getString(R.string.google_web_client_id)

    val request = remember {
        GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId) // ✅ must be WEB client id
                    .build()
            )
            .build()
    }

    Button(onClick = {
        scope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val cred = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = cred.idToken

                onIdToken(idToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }) {
        Text("Sign in with Google")
    }
}
