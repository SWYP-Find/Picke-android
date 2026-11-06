package com.picke.data.feature.auth.model

import com.picke.domain.feature.auth.model.AuthBoard
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  1&2. 소셜 로그인 및 토큰 재발급 API Response
 *
 * @param isNewUser true일 경우 신규 가입자이므로 온보딩 화면으로 라우팅 해야함
 * @param status 현재 사용자의 상태
 */
@Serializable
data class AuthResponseDto(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("user_tag")
    val userTag: String?,
    val status: String?,
    @SerialName("new_user")
    val isNewUser: Boolean?
)

/**
 * 2. 소셜 로그인 API Request
 */
@Serializable
data class SocialLoginRequest(
    val authorizationCode: String,
    val redirectUri: String
)

/**
 * 3. 로그아웃 API Response
 */
@Serializable
data class LogoutResponseDto(
    @SerialName("logged_out")
    val loggedOut: Boolean,
)

/**
 * 4. 회원 탈퇴 API Request
 *
 * @param reason 사용자가 선택한 탈퇴 사유
 */
@Serializable
data class WithdrawalRequest(
    val reason: String
)

/**
 * 4. 회원 탈퇴 API Response
 */
@Serializable
data class WithdrawnResponseDto(
    val withdrawn: Boolean,
)

/**
 * DTO -> Domain Mapper 함수
 */
fun AuthResponseDto.toDomain(): AuthBoard {
    return AuthBoard(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: "",
        userTag = userTag ?: "",
        isNewUser = isNewUser ?: false,
        status = status ?: ""
    )
}