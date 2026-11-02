package com.picke.presentation.ui.classroom.classmyopinion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionMutation
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiAction
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiEvent
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState
import com.picke.presentation.ui.classroom.classmyopinion.model.toUiModel
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
class ClassMyOpinionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ClassMyOpinionUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassMyOpinionUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassMyOpinionUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassMyOpinionUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchMyComments()
        fetchReplies()
    }

    fun onAction(action: ClassMyOpinionUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassMyOpinionUiState, action: ClassMyOpinionUiAction) {
        when (action) {
            ClassMyOpinionUiAction.BackClick -> sendEvent(ClassMyOpinionUiEvent.NavigateBack)

            ClassMyOpinionUiAction.RepliesEndReach -> if (state.canLoadMoreReplies) {
                fetchMoreReplies(state)
            }
        }
    }

    private fun sendEvent(event: ClassMyOpinionUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchMyComments() {
        // TODO: API 연동 - 클래스에서 내가 남긴 댓글 조회로 교체
        val mutation = ClassMyOpinionMutation.MyCommentsLoaded(DummyData.dummyClassMyComments)
        _uiState.update { reduce(it, mutation) }
    }

    private fun fetchReplies() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val members = classroomUseCases.getClassMembersUseCase(classId).getOrElse {
                sendEvent(ClassMyOpinionUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val activity = classroomUseCases.getClassResultMyActivityUseCase(classId).getOrElse {
                sendEvent(ClassMyOpinionUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val myDisplayName = members.firstOrNull { it.me }?.displayName ?: ""
            val mutation = ClassMyOpinionMutation.RepliesLoaded(
                myDisplayName = myDisplayName,
                replies = activity.comments.map { it.toUiModel(myDisplayName) },
                nextCursor = activity.nextCursor,
                hasNext = activity.hasNext
            )
            _uiState.update { reduce(it, mutation) }
        }
    }

    private fun fetchMoreReplies(state: ClassMyOpinionUiState) {
        viewModelScope.launch {
            classroomUseCases.getClassResultMyActivityUseCase(state.classId, state.nextCursor)
                .onSuccess { activity ->
                    val mutation = ClassMyOpinionMutation.MoreRepliesLoaded(
                        replies = activity.comments.map { it.toUiModel(state.myDisplayName) },
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