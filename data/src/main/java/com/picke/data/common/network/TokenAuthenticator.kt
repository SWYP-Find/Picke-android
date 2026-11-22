package com.picke.data.common.network

import android.util.Log
import com.picke.data.common.error.reportIfUnexpected
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    companion object {
        private const val TAG = "TokenAuthenticator_Picke"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (unauthorizedCount(response) >= 2) {
            Log.e(TAG, "[AUTH] 토큰 갱신 후에도 401 지속. 인증 실패 처리.")
            return null
        }

        val sentAccessToken = response.request.header(AuthHeader.AUTHORIZATION)
            ?.removePrefix(AuthHeader.BEARER_PREFIX)
            ?: return null

        return try {
            val accessToken = tokenRefresher.refresh(sentAccessToken) ?: return null
            response.request.newBuilder()
                .header(AuthHeader.AUTHORIZATION, "${AuthHeader.BEARER_PREFIX}$accessToken")
                .build()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "[AUTH] 토큰 갱신 중 예외 발생: ${e.message}", e)
            reportIfUnexpected(e)
            null
        }
    }

    private fun unauthorizedCount(response: Response): Int =
        generateSequence(response) { it.priorResponse }.count { it.code == 401 }
}