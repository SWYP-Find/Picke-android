package com.picke.presentation.ui.vote.model

data class VoteUiState(
    val isLoading: Boolean = false,
    val battleDetail: BattleDetailUiModel? = null,
    val isInsufficientPoints: Boolean = false,
    val isRequesting: Boolean = false
)