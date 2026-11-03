package com.picke.presentation.ui.my.setting.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.mypage.model.NotificationSettingsBoard
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.presentation.ui.my.setting.alarm.model.SettingAlarmUiEvent
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

data class SettingAlarmUiState(
    val isLoading: Boolean = false,
    val settings: NotificationSettingsBoard? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class SettingAlarmViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingAlarmUiState())
    val uiState: StateFlow<SettingAlarmUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SettingAlarmUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<SettingAlarmUiEvent> = _uiEvent.receiveAsFlow()

    init {
        loadNotificationSettings()
    }

    private fun loadNotificationSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            myPageUseCases.getNotificationSettingsUseCase()
                .onSuccess { settings ->
                    _uiState.update { it.copy(isLoading = false, settings = settings) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun updateSetting(updated: NotificationSettingsBoard) {
        val previous = _uiState.value.settings ?: return
        // 낙관적 업데이트: 즉시 UI 반영 후 API 실패 시 롤백
        _uiState.update { it.copy(settings = updated) }
        viewModelScope.launch {
            myPageUseCases.updateNotificationSettingsUseCase(updated)
                .onFailure {
                    _uiState.update { it.copy(settings = previous) }
                    _uiEvent.send(SettingAlarmUiEvent.ShowActionFailedToast)
                }
        }
    }
}
