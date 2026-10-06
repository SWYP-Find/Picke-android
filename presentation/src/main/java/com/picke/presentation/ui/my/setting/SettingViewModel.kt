package com.picke.presentation.ui.my.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.auth.usecase.AuthUseCases
import com.picke.presentation.R
import com.picke.presentation.analytics.AnalyticsScreen
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.UiActionName
import com.picke.presentation.ui.my.setting.model.SettingUiEvent
import com.picke.presentation.ui.my.setting.model.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SettingUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<SettingUiEvent> = _uiEvent.receiveAsFlow()

    fun logout() {
        analyticsTracker.trackUiAction(UiActionName.SETTINGS_LOGOUT, AnalyticsScreen.SETTINGS)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authUseCases.logoutUseCase(localPreferencesUseCases.getFcmToken())
                .onSuccess {
                    analyticsTracker.onLogout()
                    _uiEvent.send(SettingUiEvent.NavigateToLogin)
                }
                .onFailure {
                    _uiEvent.send(SettingUiEvent.ShowToast(R.string.setting_logout_error))
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun withdraw(selectedKoreanReason: String) {
        analyticsTracker.trackUiAction(UiActionName.SETTINGS_WITHDRAW, AnalyticsScreen.WITHDRAW)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authUseCases.withdrawUseCase(selectedKoreanReason)
                .onSuccess {
                    analyticsTracker.onLogout()
                    _uiEvent.send(SettingUiEvent.NavigateToLogin)
                }
                .onFailure {
                    _uiEvent.send(SettingUiEvent.ShowToast(R.string.setting_withdraw_error))
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}