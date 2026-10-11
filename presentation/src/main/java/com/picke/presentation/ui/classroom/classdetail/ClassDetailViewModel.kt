package com.picke.presentation.ui.classroom.classdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailMutation
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiAction
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiEvent
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiState
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
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
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
            ClassDetailUiAction.BattleClick -> sendEvent(ClassDetailUiEvent.NavigateToBattle(state.classId))
            ClassDetailUiAction.ViewCodeClick -> sendEvent(ClassDetailUiEvent.NavigateToShare(state.classId))
            ClassDetailUiAction.ResultClick -> sendEvent(ClassDetailUiEvent.NavigateToResult(state.classId))
            ClassDetailUiAction.DeadlineEditClick -> sendEvent(ClassDetailUiEvent.NavigateToEdit(state.classId))
            ClassDetailUiAction.ManageEditClick -> sendEvent(ClassDetailUiEvent.NavigateToEdit(state.classId))
            ClassDetailUiAction.ManageShareCodeClick -> sendEvent(ClassDetailUiEvent.NavigateToShare(state.classId))
            ClassDetailUiAction.DeleteConfirm -> deleteClass()
            ClassDetailUiAction.LeaveConfirm -> leaveClass()
            is ClassDetailUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassDetailUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassDetail() {
        // TODO: API 연동 - 클래스 상세 조회로 교체 (운영자 여부 포함)
        val classItem =
            DummyData.dummyMyClasses.firstOrNull { it.id == _uiState.value.classId } ?: return
        val mutation = ClassDetailMutation.ClassDetailLoaded(
            isOperator = classItem.isOperator,
            isInProgress = classItem.isInProgress,
            title = classItem.title,
            memberCount = classItem.memberCount,
            deadline = classItem.deadline,
            battle = DummyData.dummyClassBattles.first()
        )
        _uiState.update { reduce(it, mutation) }
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