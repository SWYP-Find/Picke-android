package com.picke.presentation.ui.perspective

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.domain.feature.perspective.usecase.ReportPerspectiveResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.R
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.ui.perspective.model.PerspectiveSort
import com.picke.presentation.ui.perspective.model.PerspectiveUiEvent
import com.picke.presentation.ui.perspective.model.PerspectiveUiState
import com.picke.presentation.ui.perspective.model.toUiModel
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
class PerspectiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val voteUseCases: VoteUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val battleId: Long = savedStateHandle.get<String>(AppRoute.ARG_BATTLE_ID)?.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(
        PerspectiveUiState(
            battleId = battleId,
            perspectives = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<PerspectiveUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<PerspectiveUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchPerspectives()
        fetchVoteStats()
        fetchMyPerspective()
        fetchMyVoteHistory()
    }

    fun updateSort(newSort: PerspectiveSort) {
        if (_uiState.value.sort == newSort) return

        _uiState.update {
            it.copy(
                sort = newSort,
                nextCursor = null,
                hasNext = true,
                perspectives = emptyList()
            )
        }
        fetchPerspectives(isRefresh = true)
    }

    fun selectOption(optionId: Long?) {
        if (_uiState.value.selectedOptionId == optionId) return

        _uiState.update {
            it.copy(
                selectedOptionId = optionId,
                nextCursor = null,
                hasNext = true,
                perspectives = emptyList()
            )
        }
        fetchPerspectives(isRefresh = true)
    }

    fun setEditMode(perspectiveId: Long?) {
        _uiState.update { it.copy(editingPerspectiveId = perspectiveId) }
    }

    fun submitPerspective(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val editId = _uiState.value.editingPerspectiveId

        _uiState.update { it.copy(editingPerspectiveId = null) }

        viewModelScope.launch {
            val result = if (editId != null) {
                perspectiveUseCases.updatePerspectiveUseCase(editId, content)
            } else {
                perspectiveUseCases.createPerspectiveUseCase(battleId, content)
            }
            result
                .onSuccess {
                    if (editId == null) {
                        analyticsTracker.trackCommunityAction(battleId.toString(), content.length)
                    }
                    onSuccess()
                    fetchMyPerspective()
                    fetchPerspectives(isRefresh = true)
                }
                .onFailure {
                    fetchMyPerspective()
                }
        }
    }

    fun deletePerspective(perspectiveId: Long) {
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.perspectiveId != perspectiveId },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        viewModelScope.launch {
            perspectiveUseCases.deletePerspectiveUseCase(perspectiveId)
                .onFailure {
                    fetchMyPerspective()
                    fetchPerspectives(isRefresh = true)
                }
        }
    }

    fun toggleLike(perspectiveId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            perspectiveUseCases.togglePerspectiveLikeUseCase(perspectiveId, isCurrentlyLiked)
                .onSuccess { toggleData ->
                    _uiState.update { state ->
                        state.copy(
                            perspectives = state.perspectives.map { item ->
                                if (item.perspectiveId == perspectiveId) {
                                    item.copy(
                                        likeCount = toggleData.likeCount,
                                        isLiked = toggleData.isLiked
                                    )
                                } else {
                                    item
                                }
                            }
                        )
                    }
                }
        }
    }

    fun reportPerspective(perspectiveId: Long) {
        viewModelScope.launch {
            perspectiveUseCases.reportPerspectiveUseCase(perspectiveId)
                .onSuccess { result ->
                    when (result) {
                        is ReportPerspectiveResult.Reported -> {
                            sendEvent(PerspectiveUiEvent.ShowToast(R.string.comment_report_success))
                        }

                        is ReportPerspectiveResult.AlreadyReported -> {
                            sendEvent(PerspectiveUiEvent.ShowToast(R.string.comment_report_duplicate))
                        }
                    }
                }
        }
    }

    fun refreshAllData() {
        fetchMyVoteHistory()
        fetchVoteStats()
        fetchMyPerspective()
        fetchPerspectives(isRefresh = true)
    }

    private fun sendEvent(event: PerspectiveUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    fun fetchPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (!isRefresh && (state.isLoading || !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            perspectiveUseCases.loadPerspectivesUseCase(
                battleId = battleId,
                cursor = cursor,
                size = 10,
                optionId = state.selectedOptionId,
                sort = state.sort.apiValue
            ).onSuccess { page ->
                val newItems = page.items.map { it.toUiModel() }
                val actualHasNext = page.hasNext && page.nextCursor != null && newItems.isNotEmpty()

                _uiState.update { currentState ->
                    val mergedList =
                        if (isRefresh) newItems else (currentState.perspectives + newItems).distinctBy { it.perspectiveId }
                    currentState.copy(
                        perspectives = mergedList,
                        nextCursor = page.nextCursor,
                        hasNext = actualHasNext,
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun fetchMyVoteHistory() {
        viewModelScope.launch {
            voteUseCases.getMyVoteHistoryUseCase(battleId)
                .onSuccess { voteHistory ->
                    _uiState.update {
                        it.copy(
                            opinionChanged = voteHistory.opinionChanged,
                            battleTitle = voteHistory.battleTitle
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(opinionChanged = false) }
                }
        }
    }

    private fun fetchMyPerspective() {
        viewModelScope.launch {
            perspectiveUseCases.getMyPerspectiveUseCase(battleId)
                .onSuccess { myData ->
                    _uiState.update { it.copy(myPerspective = myData.toUiModel()) }
                }
                .onFailure {
                    _uiState.update { it.copy(myPerspective = null) }
                }
        }
    }

    private fun fetchVoteStats() {
        viewModelScope.launch {
            voteUseCases.getVoteStatsUseCase(battleId)
                .onSuccess { statsBoard ->
                    _uiState.update { it.copy(voteOptions = statsBoard.options.map { it.toUiModel() }) }
                }
        }
    }
}