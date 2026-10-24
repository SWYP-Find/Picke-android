package com.picke.presentation.ui.classroom.classmember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberMutation
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiAction
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiEvent
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiState
import com.picke.presentation.ui.classroom.classmember.model.toUiModel
import com.picke.presentation.ui.classroom.model.ClassErrorCode
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
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
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
            is ClassMemberUiAction.StateOnly -> Unit

            ClassMemberUiAction.BackClick -> if (state.isMembersChanged) {
                sendEvent(ClassMemberUiEvent.NavigateBackAfterMembersChanged)
            } else {
                sendEvent(ClassMemberUiEvent.NavigateBack)
            }

            ClassMemberUiAction.NameConfirm -> if (state.isNameConfirmEnabled) {
                updateMyName(state.editingName.trim())
            }

            ClassMemberUiAction.RemoveConfirm -> if (state.isRemoveConfirmEnabled) {
                state.removingMember?.let { member -> removeMember(member.memberId) }
            }
        }
    }

    private fun sendEvent(event: ClassMemberUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchMembers() {
        viewModelScope.launch {
            classroomUseCases.getClassMembersUseCase(_uiState.value.classId)
                .onSuccess { memberBoards ->
                    val members = memberBoards.map { it.toUiModel() }
                    val mutation = ClassMemberMutation.MembersLoaded(
                        isOperatorViewer = members.any { it.isMe && it.isOperator },
                        members = members
                    )
                    _uiState.update { reduce(it, mutation) }
                }
                .onFailure { sendEvent(ClassMemberUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun updateMyName(name: String) {
        viewModelScope.launch {
            classroomUseCases.updateMyDisplayNameUseCase(_uiState.value.classId, name)
                .onSuccess { member ->
                    _uiState.update {
                        reduce(it, ClassMemberMutation.MyNameUpdated(member.displayName))
                    }
                }
                .onFailure { exception ->
                    _uiState.update { reduce(it, ClassMemberMutation.RequestFailed) }
                    val event =
                        if ((exception as? ApiErrorException)?.code == ClassErrorCode.CLASS_409_NAME) {
                            ClassMemberUiEvent.ShowNameDuplicatedToast
                        } else {
                            ClassMemberUiEvent.ShowActionFailedToast
                        }
                    sendEvent(event)
                }
        }
    }

    private fun removeMember(memberId: Long) {
        viewModelScope.launch {
            classroomUseCases.removeMemberUseCase(_uiState.value.classId, memberId)
                .onSuccess {
                    _uiState.update { reduce(it, ClassMemberMutation.MemberRemoved(memberId)) }
                }
                .onFailure {
                    _uiState.update { reduce(it, ClassMemberMutation.RequestFailed) }
                    sendEvent(ClassMemberUiEvent.ShowActionFailedToast)
                }
        }
    }
}