package com.picke.presentation.ui.login

import android.content.Context
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause

fun loginWithKakao(
    context: Context,
    onAuthCodeReceive: (String) -> Unit,
    onFailure: () -> Unit,
    onCancel: () -> Unit
) {
    val accountCallback: (String?, Throwable?) -> Unit = { authCode, error ->
        if (error != null) {
            onFailure()
        } else if (authCode != null) {
            onAuthCodeReceive(authCode)
        }
    }

    if (AuthCodeClient.instance.isKakaoTalkLoginAvailable(context)) {
        AuthCodeClient.instance.authorizeWithKakaoTalk(context) { authCode, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    onCancel()
                    return@authorizeWithKakaoTalk
                }
                AuthCodeClient.instance.authorizeWithKakaoAccount(
                    context = context,
                    prompts = listOf(Prompt.LOGIN),
                    callback = accountCallback
                )
            } else if (authCode != null) {
                onAuthCodeReceive(authCode)
            }
        }
    } else {
        AuthCodeClient.instance.authorizeWithKakaoAccount(
            context = context,
            prompts = listOf(Prompt.LOGIN),
            callback = accountCallback
        )
    }
}