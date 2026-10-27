package com.picke.presentation.ui.classroom.classvote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteMutation
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiAction
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiEvent
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiState
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.toUiModel
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
class ClassVoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassVoteUiState(
            classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L,
            voteType = savedStateHandle.get<String>(AppRoute.ARG_VOTE_TYPE)
                ?.let { voteType -> VoteType.valueOf(voteType) }
                ?: VoteType.PRE
        )
    )
    val uiState: StateFlow<ClassVoteUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassVoteUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassVoteUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchClassVote()
    }

    fun onAction(action: ClassVoteUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassVoteUiState, action: ClassVoteUiAction) {
        when (action) {
            ClassVoteUiAction.BackClick -> sendEvent(ClassVoteUiEvent.NavigateBack)

            is ClassVoteUiAction.VoteClick -> if (!state.isRequesting) {
                submitVote(state, action.optionId)
            }
        }
    }

    private fun sendEvent(event: ClassVoteUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassVote() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classDetail = classroomUseCases.getClassDetailUseCase(classId).getOrElse {
                sendEvent(ClassVoteUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val classBattle = classroomUseCases.getClassBattleUseCase(classId).getOrElse {
                sendEvent(ClassVoteUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val mutation = ClassVoteMutation.ClassVoteLoaded(
                className = classDetail.name,
                deadline = parseServerDateTime(classBattle.closeAt),
                battleId = classBattle.battle.battleInfo.battleId.toLongOrNull() ?: 0L,
                isAiBattle = ClassBattleType.from(classDetail.battleType) == ClassBattleType.CUSTOM,
                battle = classBattle.battle.toUiModel()
            )
            _uiState.update { reduce(it, mutation) }
        }
    }

    private fun submitVote(state: ClassVoteUiState, optionId: String) {
        val selectedOptionId = optionId.toLongOrNull() ?: 0L
        viewModelScope.launch {
            val result = when (state.voteType) {
                VoteType.PRE -> classroomUseCases.submitClassPreVoteUseCase(
                    state.classId,
                    selectedOptionId
                )

                VoteType.POST -> classroomUseCases.submitClassPostVoteUseCase(
                    state.classId,
                    selectedOptionId
                )
            }
            result
                .onSuccess { sendEvent(nextEvent(state)) }
                .onFailure {
                    _uiState.update { reduce(it, ClassVoteMutation.RequestFailed) }
                    sendEvent(ClassVoteUiEvent.ShowActionFailedToast)
                }
        }
    }

    private fun nextEvent(state: ClassVoteUiState): ClassVoteUiEvent = when (state.voteType) {
        VoteType.PRE -> if (state.isAiBattle) {
            ClassVoteUiEvent.NavigateToDebate(state.classId)
        } else {
            ClassVoteUiEvent.NavigateToScenario(state.classId, state.battleId)
        }

        VoteType.POST -> ClassVoteUiEvent.NavigateToPerspective(state.classId, state.battleId)
    }
}