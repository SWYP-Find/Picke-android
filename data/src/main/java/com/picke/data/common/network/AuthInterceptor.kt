package com.picke.data.common.network

import com.picke.data.BuildConfig
import com.picke.data.common.local.PreferencesManager
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal object AuthHeader {
    const val AUTHORIZATION = "Authorization"
    const val BEARER_PREFIX = "Bearer "
}

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    private val apiHost: String? = BuildConfig.BASE_URL.toHttpUrlOrNull()?.host

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlPath = originalRequest.url.encodedPath

        val isApiHost = originalRequest.url.host == apiHost
        val isAuthRequest = urlPath.contains("/api/v1/auth/login") || urlPath.contains("/api/v1/auth/refresh")
        val accessToken = preferencesManager.getAccessToken()

        if (!isApiHost || accessToken.isNullOrEmpty() || isAuthRequest) {
            return chain.proceed(originalRequest)
        }

        val authorizedRequest = originalRequest.newBuilder()
            .header(AuthHeader.AUTHORIZATION, "${AuthHeader.BEARER_PREFIX}$accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }
}