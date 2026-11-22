package com.picke.presentation.ui.my.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.mypage.model.MyContentActivityItem
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.presentation.ui.my.content.model.ContentActivityUiEvent
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

data class ContentActivityUiState(
    val commentList: List<MyContentActivityItem> = emptyList(),
    val commentOffset: Int? = 0,
    val hasMoreComments: Boolean = true,

    val likeList: List<MyContentActivityItem> = emptyList(),
    val likeOffset: Int? = 0,
    val hasMoreLikes: Boolean = true,

    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false
)

@HiltViewModel
class ContentActivityViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentActivityUiState())
    val uiState: StateFlow<ContentActivityUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ContentActivityUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ContentActivityUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchContentActivity()
    }

    private fun fetchContentActivity() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val commentsDeferred = async {
                myPageUseCases.getMyContentActivitiesUseCase(
                    offset = 0,
                    size = 20,
                    activityType = "COMMENT"
                )
            }
            val likesDeferred = async {
                myPageUseCases.getMyContentActivitiesUseCase(
                    offset = 0,
                    size = 20,
                    activityType = "LIKE"
                )
            }

            val commentsResult = commentsDeferred.await()
            val likesResult = likesDeferred.await()

            val comments = commentsResult.getOrNull()?.items ?: emptyList()
            val likes = likesResult.getOrNull()?.items ?: emptyList()

            _uiState.update { state ->
                state.copy(
                    commentList = commentsResult.getOrNull()?.items ?: emptyList(),
                    commentOffset = commentsResult.getOrNull()?.nextOffset,
                    hasMoreComments = commentsResult.getOrNull()?.hasNext ?: false,

                    likeList = likesResult.getOrNull()?.items ?: emptyList(),
                    likeOffset = likesResult.getOrNull()?.nextOffset,
                    hasMoreLikes = likesResult.getOrNull()?.hasNext ?: false,

                    isLoading = false
                )
            }

            if (commentsResult.isFailure || likesResult.isFailure) {
                _uiEvent.send(ContentActivityUiEvent.ShowLoadFailedToast)
            }
        }
    }

    fun loadMore(activityType: String) {
        val currentState = _uiState.value

        if (currentState.isPagingLoading) return

        val (currentOffset, hasNext) = if (activityType == "COMMENT") {
            currentState.commentOffset to currentState.hasMoreComments
        } else {
            currentState.likeOffset to currentState.hasMoreLikes
        }

        if (!hasNext || currentOffset == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPagingLoading = true) }

            val result = myPageUseCases.getMyContentActivitiesUseCase(
                offset = currentOffset,
                size = 20,
                activityType = activityType
            )

            result.onSuccess { pageData ->
                _uiState.update { state ->
                    if (activityType == "COMMENT") {
                        state.copy(
                            commentList = state.commentList + pageData.items,
                            commentOffset = pageData.nextOffset,
                            hasMoreComments = pageData.hasNext,
                            isPagingLoading = false
                        )
                    } else {
                        state.copy(
                            likeList = state.likeList + pageData.items,
                            likeOffset = pageData.nextOffset,
                            hasMoreLikes = pageData.hasNext,
                            isPagingLoading = false
                        )
                    }
                }
            }.onFailure {
                _uiState.update { it.copy(isPagingLoading = false) }
                _uiEvent.send(ContentActivityUiEvent.ShowLoadFailedToast)
            }
        }
    }
}

