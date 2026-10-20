package com.picke.presentation.ui.perspective.model

import com.picke.domain.feature.vote.model.VoteStatsOptionBoard

data class PerspectiveUiState(
    val battleId: Long = 0L,
    val voteOptions: List<VoteStatsOptionBoard> = emptyList(),
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val myPerspective: PerspectiveUiModel? = null,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val sort: PerspectiveSort = PerspectiveSort.POPULAR,
    val selectedOptionId: Long? = null,
    val opinionChanged: Boolean = false,
    val editingPerspectiveId: Long? = null,
    val battleTitle: String = ""
)