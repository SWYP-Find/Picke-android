package com.picke.presentation.ui.perspective.model

data class PerspectiveUiState(
    val battleId: Long = 0L,
    val isClassBattle: Boolean = false,
    val classSetting: PerspectiveClassSettingUiModel? = null,
    val voteOptions: List<PerspectiveVoteOptionUiModel>? = null,
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val myPerspective: PerspectiveUiModel? = null,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = true,
    val isListLoading: Boolean = false,
    val sort: PerspectiveSort = PerspectiveSort.POPULAR,
    val selectedOptionId: Long? = null,
    val opinionChanged: Boolean = false,
    val editingPerspectiveId: Long? = null,
    val battleTitle: String = ""
)