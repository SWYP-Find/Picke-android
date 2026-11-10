package com.picke.presentation.ui.comment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.comment.usecase.CommentUseCases
import com.picke.domain.feature.comment.usecase.ReportCommentResult
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.domain.feature.perspective.usecase.ReportPerspectiveResult
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.ui.comment.model.CommentUiEvent
import com.picke.presentation.ui.comment.model.CommentUiState
import com.picke.presentation.ui.comment.model.toUiModel
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
class CommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentUseCases: CommentUseCases,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val targetId: Long =
        savedStateHandle.get<String>(AppRoute.ARG_PERSPECTIVE_ID)?.toLongOrNull() ?: 0L
    private val firstOptionId: Long = savedStateHandle[AppRoute.ARG_FIRST_OPTION_ID] ?: 0L

    private val _uiState = MutableStateFlow(
        CommentUiState(
            targetId = targetId,
            firstOptionId = firstOptionId,
            comments = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CommentUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<CommentUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchInitialData()
    }

    fun submitComment(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return
        if (_uiState.value.isEditingMainPerspective) {
            updateMainPerspective(content, onSuccess)
            return
        }

        val editId = _uiState.value.editingCommentId
        val isEditMode = editId != null

        _uiState.update { it.copy(editingCommentId = null) }

        viewModelScope.launch {
            val result = if (editId != null) {
                commentUseCases.updateCommentUseCase(targetId, editId, content)
            } else {
                commentUseCases.createCommentUseCase(targetId, content)
            }
            result
                .onSuccess {
                    if (!isEditMode) {
                        analyticsTracker.trackCommunityAction(targetId.toString(), content.length)
                        fetchMainPerspective()
                    }
                    onSuccess()
                    fetchComments()
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            commentUseCases.deleteCommentUseCase(targetId, commentId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            comments = state.comments.filter { it.commentId != commentId }
                        )
                    }
                    fetchMainPerspective()
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun toggleLike(commentId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            commentUseCases.toggleCommentLikeUseCase(commentId, isCurrentlyLiked)
                .onSuccess { toggleData ->
                    _uiState.update { state ->
                        state.copy(
                            comments = state.comments.map { item ->
                                if (item.commentId == commentId) {
                                    item.copy(
                                        likeCount = toggleData.likeCount,
                                        isLiked = toggleData.isLiked
                                    )
                                } else item
                            }
                        )
                    }
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun toggleMainPerspectiveLike() {
        val mainItem = _uiState.value.mainPerspective ?: return
        viewModelScope.launch {
            perspectiveUseCases.togglePerspectiveLikeUseCase(targetId, mainItem.isLiked)
                .onSuccess { toggleData ->
                    _uiState.update { state ->
                        state.copy(
                            mainPerspective = state.mainPerspective?.copy(
                                likeCount = toggleData.likeCount,
                                isLiked = toggleData.isLiked
                            )
                        )
                    }
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun setEditMode(commentId: Long?) {
        _uiState.update { it.copy(editingCommentId = commentId, isEditingMainPerspective = false) }
    }

    fun setMainPerspectiveEditMode() {
        _uiState.update { it.copy(editingCommentId = null, isEditingMainPerspective = true) }
    }

    fun deleteMainPerspective() {
        viewModelScope.launch {
            perspectiveUseCases.deletePerspectiveUseCase(targetId)
                .onSuccess {
                    sendEvent(CommentUiEvent.NavigateBack)
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun reportMainPerspective() {
        viewModelScope.launch {
            perspectiveUseCases.reportPerspectiveUseCase(targetId)
                .onSuccess { result ->
                    when (result) {
                        is ReportPerspectiveResult.Reported -> {
                            sendEvent(CommentUiEvent.ShowReportedToast)
                        }

                        is ReportPerspectiveResult.AlreadyReported -> {
                            sendEvent(CommentUiEvent.ShowAlreadyReportedToast)
                        }
                    }
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            commentUseCases.reportCommentUseCase(targetId, commentId)
                .onSuccess { result ->
                    when (result) {
                        is ReportCommentResult.Reported -> {
                            sendEvent(CommentUiEvent.ShowReportedToast)
                        }

                        is ReportCommentResult.AlreadyReported -> {
                            sendEvent(CommentUiEvent.ShowAlreadyReportedToast)
                        }
                    }
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }

    fun refreshAllData() {
        fetchMainPerspective()
        fetchComments()
    }

    private fun sendEvent(event: CommentUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchComments() {
        if (_uiState.value.isListLoading) return

        _uiState.update { it.copy(isListLoading = true) }

        viewModelScope.launch {
            commentUseCases.loadCommentsUseCase(targetId, null, size = 10)
                .onSuccess { page ->
                    _uiState.update {
                        it.copy(
                            comments = page.items.map { comment -> comment.toUiModel() },
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isListLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isListLoading = false) }
                    sendEvent(CommentUiEvent.ShowLoadFailedToast)
                }
        }
    }

    fun fetchNextComments() {
        val state = _uiState.value
        if (state.isListLoading || !state.hasNext) return

        _uiState.update { it.copy(isListLoading = true) }

        viewModelScope.launch {
            commentUseCases.loadCommentsUseCase(targetId, state.nextCursor, size = 10)
                .onSuccess { page ->
                    val newItems = page.items.map { comment -> comment.toUiModel() }
                    _uiState.update {
                        it.copy(
                            comments = (it.comments + newItems).distinctBy { item -> item.commentId },
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isListLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isListLoading = false) }
                    sendEvent(CommentUiEvent.ShowLoadFailedToast)
                }
        }
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            val mainPerspectiveDeferred =
                async { perspectiveUseCases.loadMainPerspectiveUseCase(targetId) }
            val commentsDeferred =
                async { commentUseCases.loadCommentsUseCase(targetId, null, size = 10) }
            val mainPerspective = mainPerspectiveDeferred.await().getOrNull()
            val page = commentsDeferred.await().getOrNull()

            if (mainPerspective == null || page == null) {
                _uiState.update { it.copy(isLoading = false) }
                sendEvent(CommentUiEvent.ShowLoadFailedToast)
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    mainPerspective = mainPerspective.toUiModel(),
                    comments = page.items.map { comment -> comment.toUiModel() },
                    nextCursor = page.nextCursor,
                    hasNext = page.hasNext
                )
            }
        }
    }

    private fun fetchMainPerspective() {
        viewModelScope.launch {
            perspectiveUseCases.loadMainPerspectiveUseCase(targetId)
                .onSuccess { perspective ->
                    _uiState.update { state ->
                        state.copy(
                            mainPerspective = perspective.toUiModel()
                        )
                    }
                }
                .onFailure { sendEvent(CommentUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun updateMainPerspective(content: String, onSuccess: () -> Unit) {
        _uiState.update { it.copy(isEditingMainPerspective = false) }

        viewModelScope.launch {
            perspectiveUseCases.updatePerspectiveUseCase(targetId, content)
                .onSuccess {
                    onSuccess()
                    fetchMainPerspective()
                }
                .onFailure { sendEvent(CommentUiEvent.ShowActionFailedToast) }
        }
    }
}