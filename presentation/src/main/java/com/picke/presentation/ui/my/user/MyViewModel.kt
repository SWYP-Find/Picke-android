package com.picke.presentation.ui.my.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.ui.my.user.model.MyUiState
import com.picke.presentation.ui.my.user.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases,
    private val alarmUseCases: AlarmUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState(isLoading = true))
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    fun fetchMyInfo() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            myPageUseCases.getMyPageInfoUseCase()
                .onSuccess { infoBoard ->
                    analyticsTracker.setPointBalance(infoBoard.tier.currentPoint)
                    _uiState.update {
                        it.copy(
                            profile = infoBoard.profile.toUiModel(),
                            philosopher = infoBoard.philosopher.toUiModel(),
                            tier = infoBoard.tier.toUiModel(),
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun fetchUnreadAlarmStatus() {
        _uiState.update { it.copy(isAlarmStatusLoading = true) }

        viewModelScope.launch {
            alarmUseCases.getUnreadAlarmStatusUseCase()
                .onSuccess { hasUnread ->
                    _uiState.update {
                        it.copy(
                            hasNewNotice = hasUnread,
                            isAlarmStatusLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isAlarmStatusLoading = false) }
                }
        }
    }
}