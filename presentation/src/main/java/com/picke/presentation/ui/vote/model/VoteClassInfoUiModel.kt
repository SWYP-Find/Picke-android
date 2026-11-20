package com.picke.presentation.ui.vote.model

data class VoteClassInfoUiModel(
    val className: String,
    val commentCount: Int?,
    val deadlineText: String?,
    val isAiBattle: Boolean
)