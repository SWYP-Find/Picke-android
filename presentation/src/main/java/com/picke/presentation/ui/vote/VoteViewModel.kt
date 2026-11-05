package com.picke.presentation.ui.vote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.model.BattleTarget
import com.picke.domain.feature.battle.usecase.BattleUseCases
import com.picke.domain.feature.share.usecase.ShareUseCases
import com.picke.domain.feature.vote.usecase.SubmitVoteResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.BattleStepName
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiEvent
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.ui.vote.model.toUiModel
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
class VoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleUseCases: BattleUseCases,
    private val voteUseCases: VoteUseCases,
    private val shareUseCases: ShareUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    val battleId: String = checkNotNull(savedStateHandle[AppRoute.ARG_BATTLE_ID])

    private val _uiState = MutableStateFlow(VoteUiState(isLoading = true))
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<VoteUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<VoteUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchVoteDetail()
    }

    private fun fetchVoteDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val battleIdLong = battleId.toLongOrNull() ?: 0L

            battleUseCases.getBattleDetailUseCase(battleIdLong)
                .onSuccess { detailBoard ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            battleDetail = detailBoard.toUiModel()
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.send(VoteUiEvent.ShowLoadFailedToast)
                }
        }
    }

    fun submitVote(
        voteType: VoteType,
        selectedOptionId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val optionIdLong = selectedOptionId.toLongOrNull() ?: 0L
            val battleIdLong = battleId.toLongOrNull() ?: 0L

            voteUseCases.submitVoteUseCase(
                battleId = battleIdLong,
                optionId = optionIdLong,
                isPreVote = voteType == VoteType.PRE
            )
                .onSuccess { result ->
                    when (result) {
                        is SubmitVoteResult.Success -> {
                            if (voteType == VoteType.PRE) {
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.PRE_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId
                                )
                            } else {
                                val isChanged = voteUseCases.getMyVoteHistoryUseCase(BattleTarget.General(battleIdLong))
                                    .getOrNull()?.opinionChanged
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.POST_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId,
                                    isChanged = isChanged
                                )
                            }

                            _uiState.update { it.copy(isLoading = false) }
                            onSuccess()
                        }

                        is SubmitVoteResult.InsufficientPoints -> {
                            _uiState.update {
                                it.copy(isInsufficientPoints = true, isLoading = false)
                            }
                        }
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.send(VoteUiEvent.ShowActionFailedToast)
                }
        }
    }

    fun getShareLink(
        battleId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            shareUseCases.getBattleShareLinkUseCase(battleId)
                .onSuccess { shareUrl ->
                    onSuccess(shareUrl.shareUrl)
                }
                .onFailure { error ->
                    onError(error.message ?: "링크를 불러오는데 실패했습니다.")
                }
        }
    }

    fun dismissPointDialog() {
        _uiState.update { it.copy(isInsufficientPoints = false) }
    }

    fun trackShare(voteType: VoteType, channel: String) {
        val target = if (voteType == VoteType.POST) ShareTarget.FINAL_VOTE else ShareTarget.BATTLE
        analyticsTracker.trackShareAction(target, channel)
    }
}