package com.picke.data.feature.auth.repository

import com.picke.data.common.local.PreferencesManager
import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.auth.datasource.AuthApi
import com.picke.data.feature.auth.model.SocialLoginRequest
import com.picke.data.feature.auth.model.WithdrawalRequest
import com.picke.data.feature.auth.model.toDomain
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.auth.model.AuthBoard
import com.picke.domain.feature.auth.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun refreshAccessToken(): Result<Unit> {
        return apiCall {
            val refreshToken = preferencesManager.getRefreshToken()
                ?: return Result.failure(Throwable("리프레쉬 토큰 없음"))

            authApi.refreshAccessToken(refreshToken)
                .toResult("토큰을 갱신하지 못했습니다.")
                .map { dto ->
                    preferencesManager.saveAccessToken(dto.accessToken)
                    preferencesManager.saveRefreshToken(dto.refreshToken)
                }
        }
    }

    override suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard> = apiCall {
        val request = SocialLoginRequest(
            authorizationCode = authCode,
            redirectUri = redirectUri
        )
        authApi.login(provider, request)
            .toResult("로그인하지 못했습니다.")
            .map { dto ->
                preferencesManager.saveAccessToken(dto.accessToken)
                preferencesManager.saveRefreshToken(dto.refreshToken)
                dto.toDomain()
            }
    }

    override suspend fun logout(): Result<Unit> = apiCall {
        authApi.logout()
            .toResult("로그아웃하지 못했습니다.")
            .map { dto ->
                if (!dto.loggedOut) throw ApiErrorException("로그아웃하지 못했습니다.")
            }
            .onSuccess { preferencesManager.clearAll() }
    }

    override suspend fun withdraw(reason: String): Result<Unit> = apiCall {
        authApi.withdraw(WithdrawalRequest(reason = reason))
            .toResult("회원 탈퇴를 하지 못했습니다.")
            .map { dto ->
                if (!dto.withdrawn) throw ApiErrorException("회원 탈퇴를 하지 못했습니다.")
            }
            .onSuccess { preferencesManager.clearAll() }
    }
}