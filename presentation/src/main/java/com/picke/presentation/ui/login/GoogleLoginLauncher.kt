package com.picke.presentation.ui.login

import android.accounts.Account
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.picke.presentation.BuildConfig
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

private const val GOOGLE_ACCOUNT_TYPE = "com.google"

private val GOOGLE_SCOPES = listOf(
    Scope(Scopes.EMAIL),
    Scope(Scopes.PROFILE),
    Scope(Scopes.OPEN_ID)
)

@Composable
fun rememberGoogleLoginLauncher(
    onAuthCodeReceive: (String) -> Unit,
    onFailure: () -> Unit,
    onCancel: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val authorizationClient = remember(context) { Identity.getAuthorizationClient(context) }
    val coroutineScope = rememberCoroutineScope()

    val onAuthCode: (String?) -> Unit = { authCode ->
        if (authCode != null) onAuthCodeReceive(authCode) else onFailure()
    }

    val authorizationLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val authorizationResult =
                    authorizationClient.getAuthorizationResultFromIntent(result.data)
                onAuthCode(authorizationResult.serverAuthCode)
            } catch (e: ApiException) {
                onFailure()
            }
        }

    val requestAuthCode: (String) -> Unit = { accountName ->
        val request = AuthorizationRequest.Builder()
            .setRequestedScopes(GOOGLE_SCOPES)
            .requestOfflineAccess(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAccount(Account(accountName, GOOGLE_ACCOUNT_TYPE))
            .build()

        authorizationClient.authorize(request)
            .addOnSuccessListener { authorizationResult ->
                val pendingIntent = authorizationResult.pendingIntent
                if (authorizationResult.hasResolution() && pendingIntent != null) {
                    authorizationLauncher.launch(
                        IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                    )
                } else {
                    onAuthCode(authorizationResult.serverAuthCode)
                }
            }
            .addOnFailureListener { onFailure() }
    }

    return {
        coroutineScope.launch {
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(
                    GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()
                )
                .build()

            try {
                val credential = credentialManager.getCredential(context, request).credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    requestAuthCode(GoogleIdTokenCredential.createFrom(credential.data).id)
                } else {
                    onFailure()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: GetCredentialCancellationException) {
                onCancel()
            } catch (e: GetCredentialException) {
                onFailure()
            } catch (e: GoogleIdTokenParsingException) {
                onFailure()
            }
        }
    }
}