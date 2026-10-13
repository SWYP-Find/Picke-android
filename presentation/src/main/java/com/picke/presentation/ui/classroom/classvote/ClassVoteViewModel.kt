package com.picke.presentation.ui.classroom.classvote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteMutation
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiAction
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiEvent
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiState
import com.picke.presentation.ui.vote.model.VoteType
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
class ClassVoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
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
        when (action) {
            ClassVoteUiAction.BackClick -> sendEvent(ClassVoteUiEvent.NavigateBack)
            is ClassVoteUiAction.VoteClick -> submitVote(_uiState.value, action.optionId)
        }
    }

    private fun sendEvent(event: ClassVoteUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassVote() {
        // TODO: API 연동 - 클래스 배틀 조회 후 battleId로 배틀 상세 조회 (기존 콘텐츠는 getBattleDetailUseCase)
        val classId = _uiState.value.classId
        val classItem = DummyData.dummyMyClasses.firstOrNull { it.id == classId } ?: return
        val classBattle = DummyData.dummyClassBattleByClassId[classId] ?: return
        val battleDetail = if (classBattle.isAiQuestion) {
            DummyData.dummyClassAiBattleDetail
        } else {
            DummyData.dummyBattleDetailList.first()
        }
        val mutation = ClassVoteMutation.ClassVoteLoaded(
            className = classItem.title,
            deadline = classItem.deadline,
            battleId = classBattle.battleId,
            isAiBattle = classBattle.isAiQuestion,
            commentCount = classBattle.commentCount,
            battle = battleDetail
        )
        _uiState.update { reduce(it, mutation) }
    }

    private fun submitVote(state: ClassVoteUiState, optionId: String) {
        // TODO: API 연동 - 사전·최종 투표 요청 성공 시 이동 (optionId 전송)
        val event = when (state.voteType) {
            VoteType.PRE -> if (state.isAiBattle) {
                ClassVoteUiEvent.NavigateToDebate(state.classId)
            } else {
                ClassVoteUiEvent.NavigateToScenario(state.classId, state.battleId)
            }

            VoteType.POST -> ClassVoteUiEvent.NavigateToPerspective(state.classId, state.battleId)
        }
        sendEvent(event)
    }
}