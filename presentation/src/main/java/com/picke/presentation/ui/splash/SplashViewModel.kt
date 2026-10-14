package com.picke.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.auth.usecase.AuthUseCases
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.presentation.analytics.AnalyticsScreen
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.OnboardingStep
import com.picke.presentation.ui.splash.model.SplashUiState
import com.picke.presentation.util.AppLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    private val analyticsTracker: AnalyticsTracker,
    private val appLifecycleObserver: AppLifecycleObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        analyticsTracker.trackScreenView(AnalyticsScreen.SPLASH)
        analyticsTracker.trackOnboardingStep(OnboardingStep.SPLASH)
        checkAutoLogin()
    }

    fun onNavigationHandled() {
        _uiState.value = SplashUiState.NavigationHandled
    }

    fun markTermsAgreed() {
        localPreferencesUseCases.saveTermsAgreed()
    }

    fun isNotificationPermissionAsked(): Boolean {
        return localPreferencesUseCases.checkNotificationPermissionAsked()
    }

    fun markNotificationPermissionAsked() {
        localPreferencesUseCases.saveNotificationPermissionAsked()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            val checkRefreshToken = localPreferencesUseCases.checkRefreshToken()
            if (checkRefreshToken) {
                val result = authUseCases.refreshAccessTokenUseCase()

                result.onSuccess {
                    val savedUserTag = localPreferencesUseCases.getUserTag()

                    if (savedUserTag != null) {
                        // 3. 믹스패널 유저 식별 + 로그인 슈퍼 프로퍼티 갱신 (DAU 집계)
                        analyticsTracker.onSessionStart(
                            savedUserTag,
                            localPreferencesUseCases.getLoginProvider()
                        )

                        // 4. 출석 체크 (콜드 스타트 - 갱신된 토큰으로 호출)
                        appLifecycleObserver.checkAttendanceIfNeeded()
                    }

                    val needsTermsAgreement = !localPreferencesUseCases.checkTermsAgreed()
                    _uiState.value = SplashUiState.NavigateToMain(needsTermsAgreement)
                }.onFailure {
                    localPreferencesUseCases.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            } else {
                _uiState.value = SplashUiState.NavigateToOnboarding
            }
        }
    }
}