package com.picke.presentation.ui.login

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.picke.presentation.BuildConfig

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
    val authorizationClient = remember(context) { Identity.getAuthorizationClient(context) }

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

    val accountLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            if (result.resultCode != Activity.RESULT_OK || accountName == null) {
                onCancel()
                return@rememberLauncherForActivityResult
            }

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
        val chooseAccountIntent = AccountManager.newChooseAccountIntent(
            null,
            null,
            arrayOf(GOOGLE_ACCOUNT_TYPE),
            null,
            null,
            null,
            null
        )
        accountLauncher.launch(chooseAccountIntent)
    }
}