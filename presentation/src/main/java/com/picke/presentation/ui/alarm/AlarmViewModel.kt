package com.picke.presentation.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.NotificationActionType
import com.picke.presentation.ui.alarm.model.AlarmUiEvent
import com.picke.presentation.ui.alarm.model.AlarmUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmsUseCases: AlarmUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AlarmUiEvent>()
    val uiEvent: SharedFlow<AlarmUiEvent> = _uiEvent.asSharedFlow()

    /** notification_action(view_list)은 화면 진입 시 1회만 전송 */
    private var isViewListTracked = false

    init {
        fetchAlarms(isRefresh = true)
    }

    // 상단 필터 칩 클릭 시 호출
    fun setCategory(categoryCode: String) {
        if (_uiState.value.selectedCategory == categoryCode) return

        _uiState.update { it.copy(selectedCategory = categoryCode) }
        fetchAlarms(isRefresh = true)
    }

    // 알림 목록 호출 (초기 로드 및 페이징 겸용)
    fun fetchAlarms(isRefresh: Boolean = false) {
        val currentState = _uiState.value

        if (isRefresh) {
            _uiState.update { it.copy(isLoading = true, page = 0, hasNext = true) }
        } else {
            if (currentState.isPagingLoading || !currentState.hasNext) return
            _uiState.update { it.copy(isPagingLoading = true) }
        }

        viewModelScope.launch {
            val targetPage = if (isRefresh) 0 else _uiState.value.page
            val category = _uiState.value.selectedCategory

            alarmsUseCases.getAlarmsUseCase(
                category = category,
                page = targetPage,
                size = 20
            ).onSuccess { data ->
                _uiState.update { state ->
                    state.copy(
                        alarmList = if (isRefresh) data.items else state.alarmList + data.items,
                        page = targetPage + 1,
                        hasNext = data.hasNext,
                        isLoading = false,
                        isPagingLoading = false
                    )
                }
                if (!isViewListTracked) {
                    isViewListTracked = true
                    analyticsTracker.trackNotificationAction(
                        action = NotificationActionType.VIEW_LIST,
                        unreadCount = data.items.count { !it.isRead }
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, isPagingLoading = false) }
                _uiEvent.emit(AlarmUiEvent.ShowLoadFailedToast)
            }
        }
    }

    // 알림 상세 조회
    fun fetchAlarmDetail(notificationId: Long) {
        viewModelScope.launch {
            val result = alarmsUseCases.getAlarmDetailUseCase(notificationId)

            result.onSuccess { detailData ->
                // TODO: 받아온 detailData를 활용해 팝업을 띄우거나 화면을 이동하는 상태 추가
            }.onFailure {
                // TODO: 실패 처리 - 알림 상세 조회 실패 시 안내 방식 결정
            }
        }
    }

    // 개별 알림 읽음 처리
    fun readAlarm(notificationId: Long) {
        analyticsTracker.trackNotificationAction(NotificationActionType.ITEM_TAP)
        viewModelScope.launch {
            alarmsUseCases.readAlarmUseCase(notificationId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            alarmList = state.alarmList.map { item ->
                                if (item.notificationId == notificationId) item.copy(isRead = true)
                                else item
                            }
                        )
                    }
                }.onFailure {
                    _uiEvent.emit(AlarmUiEvent.ShowActionFailedToast)
                }
        }
    }

    // 모두 읽음 처리 (낙관적 업데이트: 클릭 즉시 UI 반영 후 실패 시 롤백)
    fun readAllAlarms() {
        val unreadCount = _uiState.value.alarmList.count { !it.isRead }
        val previousAlarmList = _uiState.value.alarmList
        _uiState.update { state ->
            state.copy(alarmList = state.alarmList.map { it.copy(isRead = true) })
        }

        viewModelScope.launch {
            alarmsUseCases.readAllAlarmsUseCase()
                .onSuccess {
                    analyticsTracker.trackNotificationAction(
                        action = NotificationActionType.READ_ALL,
                        unreadCount = unreadCount
                    )
                }.onFailure {
                    _uiState.update { state -> state.copy(alarmList = previousAlarmList) }
                    _uiEvent.emit(AlarmUiEvent.ShowReadAllFailedToast)
                }
        }
    }
}