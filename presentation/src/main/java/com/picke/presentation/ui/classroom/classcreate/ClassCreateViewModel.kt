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
        resetDeadline()
    }

    fun onAction(action: ClassCreateUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassCreateUiState, action: ClassCreateUiAction) {
        when (action) {
            ClassCreateUiAction.BackClick -> {
                if (state.previousStep == null) sendEvent(ClassCreateUiEvent.NavigateBack)
            }

            ClassCreateUiAction.NextClick -> {
                when (state.nextStep) {
                    null -> createClass()
                    ClassCreateStep.SELECTION -> fetchRecommendedBattles()
                    ClassCreateStep.METHOD, ClassCreateStep.CONDITION, ClassCreateStep.SETTING -> Unit
                }
            }

            ClassCreateUiAction.RegenerateClick -> fetchRecommendedBattles()

            is ClassCreateUiAction.DeadlineToggle -> {
                if (action.isEnabled) resetDeadline()
            }

            is ClassCreateUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassCreateUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchRecommendedBattles() {
        // TODO: API 연동 - 조건 기반 추천 배틀 / AI 질문 생성으로 교체
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
        // TODO: API 연동 - 클래스 생성 요청 후 응답의 classId로 이동
        sendEvent(ClassCreateUiEvent.NavigateToShare(0L))
    }
}