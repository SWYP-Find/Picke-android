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
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.classroom.model.ClassRole
import com.picke.presentation.ui.classroom.model.toUiModel
import com.picke.presentation.util.parseServerDateTime
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
class ClassDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassDetailUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassDetailUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassDetailUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchClassDetail()
    }

    fun onAction(action: ClassDetailUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassDetailUiState, action: ClassDetailUiAction) {
        when (action) {
            ClassDetailUiAction.BackClick -> sendEvent(ClassDetailUiEvent.NavigateBack)
            ClassDetailUiAction.MemberClick -> sendEvent(ClassDetailUiEvent.NavigateToMember(state.classId))
            ClassDetailUiAction.BattleClick -> startBattle(state)
            ClassDetailUiAction.ResultClick -> sendEvent(ClassDetailUiEvent.NavigateToResult(state.classId))
            ClassDetailUiAction.DeadlineEditClick -> sendEvent(
                ClassDetailUiEvent.NavigateToEdit(state.classId)
            )

            ClassDetailUiAction.ManageEditClick -> sendEvent(ClassDetailUiEvent.NavigateToEdit(state.classId))
            ClassDetailUiAction.ManageShareCodeClick -> sendEvent(
                ClassDetailUiEvent.NavigateToShare(state.classId)
            )

            ClassDetailUiAction.DeleteConfirm -> deleteClass()
            ClassDetailUiAction.LeaveConfirm -> leaveClass()
            is ClassDetailUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassDetailUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassDetail() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classDetail = classroomUseCases.getClassDetailUseCase(classId).getOrElse {
                sendEvent(ClassDetailUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val mutation = ClassDetailMutation.ClassDetailLoaded(
                isOperator = ClassRole.from(classDetail.myRole) == ClassRole.OWNER,
                isInProgress = !classDetail.closed,
                title = classDetail.name,
                memberCount = classDetail.memberCount,
                deadline = parseServerDateTime(classDetail.closeAt)
            )
            _uiState.update { reduce(it, mutation) }

            val battleType = ClassBattleType.from(classDetail.battleType)
            classroomUseCases.getClassBattleUseCase(classId)
                .onSuccess { classBattle ->
                    val battle = classBattle.toUiModel(battleType)
                    _uiState.update { reduce(it, ClassDetailMutation.BattleLoaded(battle)) }
                }
                .onFailure { sendEvent(ClassDetailUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun startBattle(state: ClassDetailUiState) {
        val event = if (state.isInProgress) {
            ClassDetailUiEvent.NavigateToBattle(state.classId)
        } else {
            ClassDetailUiEvent.ShowDeadlinePassedToast
        }
        sendEvent(event)
    }

    private fun deleteClass() {
        // TODO: API 연동 - 클래스 삭제 요청 성공 시 이동
        sendEvent(ClassDetailUiEvent.NavigateBack)
    }

    private fun leaveClass() {
        // TODO: API 연동 - 클래스 나가기 요청 성공 시 이동
        sendEvent(ClassDetailUiEvent.NavigateBack)
    }
}