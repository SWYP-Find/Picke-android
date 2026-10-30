package com.picke.presentation.ui.classroom.classdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailMutation
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiAction
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiEvent
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiState
import com.picke.presentation.ui.classroom.model.ClassBattleStep
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.classroom.model.ClassRole
import com.picke.presentation.ui.classroom.model.toUiModel
import com.picke.presentation.util.parseServerDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassDetailUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassDetailUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassDetailUiEvent> = _uiEvent.receiveAsFlow()

    init {
        observeClassChanged()
        fetchClassDetail()
    }

    fun onAction(action: ClassDetailUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassDetailUiState, action: ClassDetailUiAction) {
        when (action) {
            is ClassDetailUiAction.StateOnly -> Unit

            ClassDetailUiAction.BackClick -> if (state.isClassChanged) {
                sendEvent(ClassDetailUiEvent.NavigateBackAfterClassChanged)
            } else {
                sendEvent(ClassDetailUiEvent.NavigateBack)
            }

            ClassDetailUiAction.MemberClick -> sendEvent(ClassDetailUiEvent.NavigateToMember(state.classId))

            ClassDetailUiAction.DeadlineEditClick -> sendEvent(
                ClassDetailUiEvent.NavigateToEdit(state.classId)
            )

            ClassDetailUiAction.BattleClick -> startBattle(state)

            ClassDetailUiAction.ResultClick -> sendEvent(ClassDetailUiEvent.NavigateToResult(state.classId))

            ClassDetailUiAction.ManageEditClick -> sendEvent(ClassDetailUiEvent.NavigateToEdit(state.classId))

            ClassDetailUiAction.ManageShareCodeClick -> sendEvent(
                ClassDetailUiEvent.NavigateToShare(state.classId)
            )

            ClassDetailUiAction.DeleteConfirm -> if (!state.isRequesting) deleteClass()

            ClassDetailUiAction.LeaveConfirm -> if (!state.isRequesting) leaveClass()
        }
    }

    private fun sendEvent(event: ClassDetailUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun observeClassChanged() {
        savedStateHandle.getStateFlow(AppRoute.RESULT_CLASS_CHANGED, false)
            .filter { it }
            .onEach {
                savedStateHandle[AppRoute.RESULT_CLASS_CHANGED] = false
                _uiState.update { reduce(it, ClassDetailMutation.ClassChanged) }
                fetchClassDetail()
            }
            .launchIn(viewModelScope)
    }

    private fun fetchClassDetail() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classDetail = classroomUseCases.getClassDetailUseCase(classId).getOrElse {
                sendEvent(ClassDetailUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val mutation = ClassDetailMutation.ClassDetailLoaded(
                isOperator = ClassRole.from(classDetail.myRole).isOwner,
                isInProgress = !classDetail.closed,
                title = classDetail.name,
                memberCount = classDetail.memberCount,
                deadline = parseServerDateTime(classDetail.closeAt)
            )
            _uiState.update { reduce(it, mutation) }

            val battleType = ClassBattleType.from(classDetail.battleType)
            classroomUseCases.getClassBattleUseCase(classId)
                .onSuccess { classBattle ->
                    val mutation = ClassDetailMutation.BattleLoaded(
                        battle = classBattle.toUiModel(battleType),
                        battleStep = ClassBattleStep.from(classBattle.battle.currentStep),
                        canParticipate = classBattle.canParticipate
                    )
                    _uiState.update { reduce(it, mutation) }
                }
                .onFailure { sendEvent(ClassDetailUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun startBattle(state: ClassDetailUiState) {
        val battle = state.battle
        val event = when {
            !state.isInProgress -> ClassDetailUiEvent.ShowDeadlinePassedToast

            battle == null -> ClassDetailUiEvent.ShowLoadFailedToast

            !state.canParticipate -> ClassDetailUiEvent.ShowNotParticipatingToast

            else -> battleStepEvent(state.classId, state.battleStep, battle)
        }
        sendEvent(event)
    }

    private fun battleStepEvent(
        classId: Long,
        step: ClassBattleStep,
        battle: ClassBattleUiModel
    ): ClassDetailUiEvent = when (step) {
        ClassBattleStep.NONE -> ClassDetailUiEvent.NavigateToPreVote(classId)

        ClassBattleStep.PRE_VOTE,
        ClassBattleStep.TTS_LISTENING -> if (battle.isAiQuestion) {
            ClassDetailUiEvent.NavigateToDebate(classId)
        } else {
            ClassDetailUiEvent.NavigateToScenario(classId, battle.battleId)
        }

        ClassBattleStep.POST_VOTE -> ClassDetailUiEvent.NavigateToFinalVote(classId)

        ClassBattleStep.COMPLETED ->
            ClassDetailUiEvent.NavigateToPerspective(classId, battle.battleId)
    }

    private fun deleteClass() {
        viewModelScope.launch {
            classroomUseCases.deleteClassUseCase(_uiState.value.classId)
                .onSuccess { sendEvent(ClassDetailUiEvent.NavigateBackAfterClassRemoved) }
                .onFailure {
                    _uiState.update { reduce(it, ClassDetailMutation.RequestFailed) }
                    sendEvent(ClassDetailUiEvent.ShowActionFailedToast)
                }
        }
    }

    private fun leaveClass() {
        viewModelScope.launch {
            classroomUseCases.leaveClassUseCase(_uiState.value.classId)
                .onSuccess { sendEvent(ClassDetailUiEvent.NavigateBackAfterClassRemoved) }
                .onFailure {
                    _uiState.update { reduce(it, ClassDetailMutation.RequestFailed) }
                    sendEvent(ClassDetailUiEvent.ShowActionFailedToast)
                }
        }
    }
}