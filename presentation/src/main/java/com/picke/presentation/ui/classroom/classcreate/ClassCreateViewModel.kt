package com.picke.presentation.ui.classroom.classcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMutation
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiEvent
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ClassCreateViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ClassCreateUiState())
    val uiState: StateFlow<ClassCreateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassCreateUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassCreateUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchTicketEndDate()
        resetDeadline()
    }

    fun onAction(action: ClassCreateUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassCreateUiState, action: ClassCreateUiAction) {
        when (action) {
            ClassCreateUiAction.BackClick -> if (state.previousStep == null) {
                sendEvent(ClassCreateUiEvent.NavigateBack)
            }

            ClassCreateUiAction.NextClick -> {
                when (state.nextStep) {
                    null -> createClass()
                    ClassCreateStep.SELECTION -> fetchRecommendedBattles()
                    ClassCreateStep.METHOD, ClassCreateStep.CONDITION, ClassCreateStep.SETTING -> Unit
                }
            }

            is ClassCreateUiAction.BattlePreviewClick ->
                sendEvent(ClassCreateUiEvent.NavigateToBattlePreview(action.battleId))

            ClassCreateUiAction.RegenerateClick -> fetchRecommendedBattles()

            is ClassCreateUiAction.DeadlineToggle -> if (action.isEnabled) resetDeadline()

            is ClassCreateUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassCreateUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchTicketEndDate() {
        // TODO: API 연동 - 내 이용권 종료일 조회로 교체
        _uiState.update {
            reduce(it, ClassCreateMutation.TicketEndDateLoaded(DummyData.dummyRegisteredTicket.endDate))
        }
    }

    private fun fetchRecommendedBattles() {
        // TODO: API 연동 - 조건 기반 추천 배틀 / AI 질문 생성으로 교체, 실패 시 BATTLE_SEARCH_FAILED / TOPIC_GENERATION_FAILED로 RequestFailed 반영
        val battles = if (_uiState.value.method == ClassCreateMethod.AI_TOPIC) {
            DummyData.dummyClassAiQuestions
        } else {
            DummyData.dummyClassBattles
        }
        _uiState.update { reduce(it, ClassCreateMutation.BattlesLoaded(battles)) }
    }

    private fun resetDeadline() {
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        _uiState.update { reduce(it, ClassCreateMutation.DeadlineReset(now)) }
    }

    private fun createClass() {
        val state = _uiState.value
        val deadline = state.deadline
        val isClassNameInvalid = state.className.isBlank()
        val isDeadlineOutOfRange = state.isDeadlineEnabled && deadline != null &&
            (deadline.isBefore(LocalDateTime.now()) || deadline.toLocalDate().isAfter(state.ticketEndDate ?: LocalDate.MAX))
        if (isClassNameInvalid || isDeadlineOutOfRange) {
            _uiState.update {
                reduce(it, ClassCreateMutation.SettingInvalid(isClassNameInvalid, isDeadlineOutOfRange))
            }
            return
        }

        // TODO: API 연동 - 클래스 생성 요청 후 응답의 classId로 이동, 실패 시 CLASS_CREATION_FAILED로 RequestFailed 반영
        sendEvent(ClassCreateUiEvent.NavigateToShare(DummyData.dummyMyClasses.first().id))
    }
}