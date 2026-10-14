package com.picke.presentation.ui.splash.model

sealed class SplashUiState {

    object Loading : SplashUiState()

    object NavigationHandled : SplashUiState()

    object NavigateToLogin : SplashUiState()

    object NavigateToOnboarding : SplashUiState()

    data class NavigateToMain(val needsTermsAgreement: Boolean = false) : SplashUiState()
}