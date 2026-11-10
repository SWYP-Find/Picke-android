package com.picke.presentation.ui.classroom.classcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateErrorDialog
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMutation
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiEvent
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.classcreate.model.ClassTopicSuggestStatus
import com.picke.presentation.ui.classroom.model.ClassErrorCode
import com.picke.presentation.ui.classroom.model.toUiModel
import com.picke.presentation.util.formatServerDateTime
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ClassCreateViewModel @Inject constructor(
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

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
            is ClassCreateUiAction.StateOnly -> Unit

            ClassCreateUiAction.BackClick -> if (state.previousStep == null) {
                sendEvent(ClassCreateUiEvent.NavigateBack)
            }

            ClassCreateUiAction.NextClick -> if (state.isNextEnabled) {
                when (state.nextStep) {
                    null -> createClass()
                    ClassCreateStep.SELECTION -> fetchRecommendedBattles()
                    ClassCreateStep.METHOD, ClassCreateStep.CONDITION, ClassCreateStep.SETTING -> Unit
                }
            }

            ClassCreateUiAction.RegenerateClick -> if (state.isRegenerateEnabled) {
                val draftId = state.topicDraftId
                if (draftId == null) createTopicDraft() else suggestTopics(draftId)
            }

            is ClassCreateUiAction.BattlePreviewClick ->
                sendEvent(ClassCreateUiEvent.NavigateToBattlePreview(action.battleId))

            is ClassCreateUiAction.DeadlineToggle -> if (action.isEnabled) resetDeadline()
        }
    }

    private fun sendEvent(event: ClassCreateUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchTicketEndDate() {
        viewModelScope.launch {
            classroomUseCases.getMyClassTicketUseCase()
                .onSuccess { ticket ->
                    val ticketEndDate = parseServerDateTime(ticket.expiresAt)?.toLocalDate()
                    _uiState.update {
                        reduce(it, ClassCreateMutation.TicketEndDateLoaded(ticketEndDate))
                    }
                }
                .onFailure { sendEvent(ClassCreateUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun fetchRecommendedBattles() {
        if (_uiState.value.method == ClassCreateMethod.AI_TOPIC) {
            createTopicDraft()
            return
        }
        val state = _uiState.value
        viewModelScope.launch {
            classroomUseCases.getClassBattleRecommendationsUseCase(
                difficulty = state.selectedLevel.apiValue,
                categories = state.selectedCategories.map { it.apiValue },
                keyword = state.topic.trim().ifBlank { null }
            ).onSuccess { recommendations ->
                val battles = recommendations.map { it.toUiModel() }
                _uiState.update { reduce(it, ClassCreateMutation.BattlesLoaded(battles)) }
            }.onFailure {
                _uiState.update {
                    reduce(
                        it,
                        ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.BATTLE_SEARCH_FAILED)
                    )
                }
            }
        }
    }

    private fun createTopicDraft() {
        val state = _uiState.value
        viewModelScope.launch {
            classroomUseCases.createClassTopicDraftUseCase(
                topicSeed = state.topic.trim(),
                difficulty = state.selectedLevel.apiValue,
                categories = state.selectedCategories.map { it.topicApiValue }
            ).onSuccess { draft ->
                _uiState.update {
                    reduce(
                        it,
                        ClassCreateMutation.TopicDraftCreated(
                            draft.draftId,
                            draft.remainingSuggestions
                        )
                    )
                }
                suggestTopics(draft.draftId)
            }.onFailure {
                _uiState.update {
                    reduce(
                        it,
                        ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.TOPIC_GENERATION_FAILED)
                    )
                }
            }
        }
    }

    private fun suggestTopics(draftId: Long) {
        viewModelScope.launch {
            classroomUseCases.suggestClassTopicsUseCase(draftId)
                .onSuccess { draft ->
                    val mutation = when (ClassTopicSuggestStatus.from(draft.suggestStatus)) {
                        ClassTopicSuggestStatus.SUCCEEDED -> ClassCreateMutation.TopicsSuggested(
                            batchNo = draft.batchNo,
                            remainingSuggestions = draft.remainingSuggestions,
                            battles = draft.candidates.map { it.toUiModel() }
                        )

                        ClassTopicSuggestStatus.NEEDS_CLARIFICATION -> ClassCreateMutation.TopicClarificationNeeded(
                            remainingSuggestions = draft.remainingSuggestions,
                            message = draft.clarificationMessage
                        )

                        ClassTopicSuggestStatus.NONE,
                        ClassTopicSuggestStatus.GENERATING,
                        ClassTopicSuggestStatus.FAILED -> ClassCreateMutation.RequestFailed(
                            ClassCreateErrorDialog.TOPIC_GENERATION_FAILED
                        )
                    }
                    _uiState.update { reduce(it, mutation) }
                }
                .onFailure { exception ->
                    val code = (exception as? ApiErrorException)?.code
                    val mutation = if (code == ClassErrorCode.CLASS_409_TOPIC_SUGGEST_LIMIT) {
                        ClassCreateMutation.TopicSuggestLimitReached
                    } else {
                        ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.TOPIC_GENERATION_FAILED)
                    }
                    _uiState.update { reduce(it, mutation) }
                }
        }
    }

    private fun resetDeadline() {
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
        _uiState.update { reduce(it, ClassCreateMutation.DeadlineReset(now)) }
    }

    private fun createClass() {
        val state = _uiState.value
        val deadline = state.deadline
        val isClassNameInvalid = state.className.isBlank()
        val ticketEndDate = state.ticketEndDate
        val isDeadlinePassed = deadline != null && deadline.isBefore(LocalDateTime.now())
        val isAfterTicketEnd = deadline != null && ticketEndDate != null
                && deadline.toLocalDate().isAfter(ticketEndDate)
        val isDeadlineOutOfRange = state.isDeadlineEnabled && (isDeadlinePassed || isAfterTicketEnd)
        if (isClassNameInvalid || isDeadlineOutOfRange) {
            _uiState.update {
                reduce(
                    it,
                    ClassCreateMutation.SettingInvalid(isClassNameInvalid, isDeadlineOutOfRange)
                )
            }
            return
        }

        viewModelScope.launch {
            when (state.method) {
                ClassCreateMethod.CONTENT -> requestCreateClass(
                    state = state,
                    battleId = state.selectedBattleId,
                    topicVersionId = null
                )

                ClassCreateMethod.AI_TOPIC -> createClassWithTopic(state)
            }
        }
    }

    private suspend fun requestCreateClass(
        state: ClassCreateUiState,
        battleId: Long?,
        topicVersionId: Long?
    ) {
        val deadline = state.deadline
        classroomUseCases.createClassUseCase(
            name = state.className.trim(),
            battleId = battleId,
            topicVersionId = topicVersionId,
            deadline = if (state.isDeadlineEnabled) deadline?.let(::formatServerDateTime) else null,
            commentRequired = state.isCommentRequired,
            ownerParticipates = state.isOperatorJoin
        ).onSuccess { classDetail ->
            sendEvent(ClassCreateUiEvent.NavigateToShare(classDetail.classId))
        }.onFailure { exception ->
            val mutation =
                if ((exception as? ApiErrorException)?.code == ClassErrorCode.CLASS_400_DEADLINE) {
                    ClassCreateMutation.SettingInvalid(
                        isClassNameInvalid = false,
                        isDeadlineOutOfRange = true
                    )
                } else {
                    ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.CLASS_CREATION_FAILED)
                }
            _uiState.update { reduce(it, mutation) }
        }
    }

    private suspend fun createClassWithTopic(state: ClassCreateUiState) {
        val draftId = state.topicDraftId
        val candidateId = state.selectedCandidateId
        if (draftId == null || candidateId == null) {
            _uiState.update {
                reduce(
                    it,
                    ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.CLASS_CREATION_FAILED)
                )
            }
            return
        }

        classroomUseCases.selectClassTopicCandidateUseCase(draftId, state.topicBatchNo, candidateId)
            .onSuccess { topicVersion ->
                requestCreateClass(
                    state = state,
                    battleId = null,
                    topicVersionId = topicVersion.topicVersionId
                )
            }
            .onFailure {
                _uiState.update {
                    reduce(
                        it,
                        ClassCreateMutation.RequestFailed(ClassCreateErrorDialog.CLASS_CREATION_FAILED)
                    )
                }
            }
    }
}