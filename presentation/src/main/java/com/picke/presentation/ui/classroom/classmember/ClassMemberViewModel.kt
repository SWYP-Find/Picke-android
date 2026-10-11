package com.picke.presentation.ui.classroom.classmember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberMutation
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiAction
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiEvent
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiState
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
class ClassMemberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassMemberUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassMemberUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassMemberUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassMemberUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchMembers()
    }

    fun onAction(action: ClassMemberUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassMemberUiState, action: ClassMemberUiAction) {
        when (action) {
            ClassMemberUiAction.BackClick -> sendEvent(ClassMemberUiEvent.NavigateBack)
            ClassMemberUiAction.NameConfirm -> updateMyName(state.editingName.trim())
            ClassMemberUiAction.RemoveConfirm -> state.removingMember?.let { member -> removeMember(member.memberId) }
            is ClassMemberUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassMemberUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchMembers() {
        // TODO: API 연동 - 멤버 목록 조회로 교체 (운영자 여부 포함)
        val classItem = DummyData.dummyMyClasses.firstOrNull { it.id == _uiState.value.classId }
        val isOperator = classItem?.isOperator ?: false
        val mutation = ClassMemberMutation.MembersLoaded(
            isOperatorViewer = isOperator,
            members = if (isOperator) DummyData.dummyClassMembers else DummyData.dummyParticipantClassMembers
        )
        _uiState.update { reduce(it, mutation) }
    }

    private fun updateMyName(name: String) {
        if (name.isNotEmpty()) {
            // TODO: API 연동 - 내 이름 수정 요청 성공 시 반영
            _uiState.update { reduce(it, ClassMemberMutation.MyNameUpdated(name)) }
        }
    }

    private fun removeMember(memberId: Long) {
        // TODO: API 연동 - 멤버 내보내기 요청 성공 시 반영
        _uiState.update { reduce(it, ClassMemberMutation.MemberRemoved(memberId)) }
    }
}