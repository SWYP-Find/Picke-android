package com.picke.data.common.network

import com.picke.data.common.local.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlPath = originalRequest.url.encodedPath

        val isAuthRequest = urlPath.contains("/api/v1/auth/login") || urlPath.contains("/api/v1/auth/refresh")
        val accessToken = preferencesManager.getAccessToken()

        if (accessToken.isNullOrEmpty() || isAuthRequest) {
            return chain.proceed(originalRequest)
        }

        val authorizedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }
}