package com.picke.presentation.ui.classroom.classdebate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMutation
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiAction
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiEvent
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiState
import com.picke.presentation.ui.classroom.classdebate.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ClassDebateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassDebateUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassDebateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassDebateUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassDebateUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchDebate()
    }

    fun onAction(action: ClassDebateUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassDebateUiState, action: ClassDebateUiAction) {
        when (action) {
            is ClassDebateUiAction.StateOnly -> Unit

            ClassDebateUiAction.BackClick -> sendEvent(ClassDebateUiEvent.NavigateBack)

            ClassDebateUiAction.SendClick -> if (state.isSendEnabled) {
                if (state.isReplyLengthEnough) {
                    submitReply(state)
                } else {
                    sendEvent(ClassDebateUiEvent.ShowReplyTooShortToast)
                }
            }

            ClassDebateUiAction.FinalVoteClick ->
                sendEvent(ClassDebateUiEvent.NavigateToFinalVote(state.classId))

            ClassDebateUiAction.FinalVoteDialogVoteClick ->
                sendEvent(ClassDebateUiEvent.NavigateToFinalVote(state.classId))
        }
    }

    private fun sendEvent(event: ClassDebateUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchDebate() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classBattle = classroomUseCases.getClassBattleUseCase(classId).getOrElse {
                sendEvent(ClassDebateUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val debate = startOrGetDebate(classId, classBattle.canParticipate).getOrElse {
                sendEvent(ClassDebateUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val mutation = ClassDebateMutation.DebateLoaded(
                battleTitle = classBattle.battle.battleInfo.title,
                debate = debate.toUiModel()
            )
            _uiState.update { reduce(it, mutation) }
        }
    }

    private suspend fun startOrGetDebate(
        classId: Long,
        canParticipate: Boolean
    ): Result<ClassDebateBoard> =
        if (canParticipate) {
            classroomUseCases.startClassDebateUseCase(classId)
        } else {
            classroomUseCases.getClassDebateUseCase(classId)
        }

    private fun submitReply(state: ClassDebateUiState) {
        val requestKey = state.replyRequestKey ?: UUID.randomUUID().toString()
        viewModelScope.launch {
            classroomUseCases.submitClassDebateReplyUseCase(
                state.classId,
                state.debate.round,
                state.input.trim(),
                requestKey
            ).onSuccess { debate ->
                _uiState.update {
                    reduce(it, ClassDebateMutation.ReplySubmitted(debate.toUiModel()))
                }
            }.onFailure {
                _uiState.update { reduce(it, ClassDebateMutation.ReplyFailed(requestKey)) }
                sendEvent(ClassDebateUiEvent.ShowActionFailedToast)
            }
        }
    }
}