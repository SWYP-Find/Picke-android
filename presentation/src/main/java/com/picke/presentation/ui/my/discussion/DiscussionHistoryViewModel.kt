package com.picke.presentation.ui.my.discussion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.mypage.model.MyBattleRecordItem
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.presentation.ui.my.discussion.model.DiscussionHistoryUiEvent
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

data class DiscussionHistoryUiState(
    val items: List<MyBattleRecordItem> = emptyList(),
    val nextOffset: Int? = 0,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false
)

@HiltViewModel
class DiscussionHistoryViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscussionHistoryUiState())
    val uiState: StateFlow<DiscussionHistoryUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<DiscussionHistoryUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<DiscussionHistoryUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchDiscussionHistory()
    }

    private fun fetchDiscussionHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result =
                myPageUseCases.getMyBattleRecordsUseCase(offset = 0, size = 20, voteSide = null)

            result.onFailure {
                _uiEvent.send(DiscussionHistoryUiEvent.ShowLoadFailedToast)
            }

            _uiState.update { state ->
                state.copy(
                    items = result.getOrNull()?.items ?: emptyList(),
                    nextOffset = result.getOrNull()?.nextOffset,
                    hasMore = result.getOrNull()?.hasNext ?: false,
                    isLoading = false
                )
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState.isPagingLoading) return
        if (!currentState.hasMore || currentState.nextOffset == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPagingLoading = true) }

            val result = myPageUseCases.getMyBattleRecordsUseCase(
                offset = currentState.nextOffset,
                size = 20,
                voteSide = null
            )

            result.onSuccess { pageData ->
                _uiState.update { state ->
                    state.copy(
                        items = state.items + pageData.items,
                        nextOffset = pageData.nextOffset,
                        hasMore = pageData.hasNext,
                        isPagingLoading = false
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isPagingLoading = false) }
                _uiEvent.send(DiscussionHistoryUiEvent.ShowLoadFailedToast)
            }
        }
    }
}
