package com.picke.presentation.ui.classroom.classmyopinion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionMutation
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiAction
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiEvent
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState
import com.picke.presentation.ui.classroom.classmyopinion.model.toUiModel
import com.picke.presentation.ui.perspective.model.PerspectiveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
class ClassMyOpinionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases,
    private val perspectiveUseCases: PerspectiveUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ClassMyOpinionUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassMyOpinionUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassMyOpinionUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassMyOpinionUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchMyOpinions()
    }

    fun onAction(action: ClassMyOpinionUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassMyOpinionUiState, action: ClassMyOpinionUiAction) {
        when (action) {
            ClassMyOpinionUiAction.BackClick -> sendEvent(ClassMyOpinionUiEvent.NavigateBack)

            ClassMyOpinionUiAction.CommentsEndReach -> if (state.canLoadMoreComments) {
                fetchMoreMyComments(state)
            }
        }
    }

    private fun sendEvent(event: ClassMyOpinionUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchMyOpinions() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val perspectivesDeferred = async {
                perspectiveUseCases.getMyPerspectivesUseCase(BattleTarget.Class(classId))
            }
            val membersDeferred = async { classroomUseCases.getClassMembersUseCase(classId) }
            val activityDeferred =
                async { classroomUseCases.getClassResultMyActivityUseCase(classId) }
            val perspectiveBoards = perspectivesDeferred.await().getOrNull()
            val members = membersDeferred.await().getOrNull()
            val activity = activityDeferred.await().getOrNull()

            if (perspectiveBoards == null || members == null || activity == null) {
                _uiState.update { reduce(it, ClassMyOpinionMutation.MyOpinionsLoadFailed) }
                sendEvent(ClassMyOpinionUiEvent.ShowLoadFailedToast)
                return@launch
            }

            val myDisplayName = members.firstOrNull { it.me }?.displayName ?: ""
            val mutation = ClassMyOpinionMutation.MyOpinionsLoaded(
                perspectives = perspectiveBoards
                    .filter { PerspectiveStatus.from(it.status) == PerspectiveStatus.PUBLISHED }
                    .map { it.toUiModel() },
                myDisplayName = myDisplayName,
                comments = activity.comments.map { it.toUiModel(myDisplayName) },
                nextCursor = activity.nextCursor,
                hasNext = activity.hasNext
            )
            _uiState.update { reduce(it, mutation) }
        }
    }

    private fun fetchMoreMyComments(state: ClassMyOpinionUiState) {
        viewModelScope.launch {
            classroomUseCases.getClassResultMyActivityUseCase(state.classId, state.nextCursor)
                .onSuccess { activity ->
                    val mutation = ClassMyOpinionMutation.MoreMyCommentsLoaded(
                        comments = activity.comments.map { it.toUiModel(state.myDisplayName) },
                        nextCursor = activity.nextCursor,
                        hasNext = activity.hasNext
                    )
                    _uiState.update { reduce(it, mutation) }
                }
                .onFailure {
                    _uiState.update { reduce(it, ClassMyOpinionMutation.RequestFailed) }
                    sendEvent(ClassMyOpinionUiEvent.ShowLoadFailedToast)
                }
        }
    }
}