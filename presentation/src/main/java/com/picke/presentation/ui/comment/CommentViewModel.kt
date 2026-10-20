package com.picke.presentation.ui.comment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.comment.usecase.CommentUseCases
import com.picke.domain.feature.comment.usecase.ReportCommentResult
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.R
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.ui.comment.model.CommentUiEvent
import com.picke.presentation.ui.comment.model.CommentUiState
import com.picke.presentation.ui.comment.model.toUiModel
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
class CommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentUseCases: CommentUseCases,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val targetId: Long = savedStateHandle.get<String>(AppRoute.ARG_PERSPECTIVE_ID)?.toLongOrNull() ?: 0L
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
        fetchMainPerspective()
        fetchComments()
    }

    fun submitComment(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val editId = _uiState.value.editingCommentId
        val isEditMode = editId != null

        _uiState.update { it.copy(editingCommentId = null) }

        viewModelScope.launch {
            commentUseCases.submitCommentUseCase(targetId, editId, content)
                .onSuccess {
                    if (!isEditMode) {
                        analyticsTracker.trackCommunityAction(targetId.toString(), content.length)
                    }
                    onSuccess()
                    fetchComments(isRefresh = true)
                }
        }
    }

    fun deleteComment(commentId: Long) {
        _uiState.update { state ->
            state.copy(
                comments = state.comments.filter { it.commentId != commentId }
            )
        }

        viewModelScope.launch {
            commentUseCases.deleteCommentUseCase(targetId, commentId)
                .onSuccess {
                    fetchMainPerspective()
                }
                .onFailure {
                    fetchComments(isRefresh = true)
                }
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
        }
    }

    fun setEditMode(commentId: Long?) {
        _uiState.update { it.copy(editingCommentId = commentId) }
    }

    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            commentUseCases.reportCommentUseCase(targetId, commentId)
                .onSuccess { result ->
                    when (result) {
                        is ReportCommentResult.Reported -> {
                            sendEvent(CommentUiEvent.ShowToast(R.string.comment_report_success))
                        }

                        is ReportCommentResult.AlreadyReported -> {
                            sendEvent(CommentUiEvent.ShowToast(R.string.comment_report_duplicate))
                        }
                    }
                }
        }
    }

    fun refreshAllData() {
        fetchMainPerspective()
        fetchComments(isRefresh = true)
    }

    private fun sendEvent(event: CommentUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchComments(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || (!isRefresh && !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            commentUseCases.loadCommentsUseCase(targetId, cursor, size = 10)
                .onSuccess { page ->
                    val newItems = page.items.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            comments = if (isRefresh) newItems else it.comments + newItems,
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
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
        }
    }
}