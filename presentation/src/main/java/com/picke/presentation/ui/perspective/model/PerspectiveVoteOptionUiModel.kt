package com.picke.presentation.ui.perspective.model

import com.picke.domain.feature.vote.model.VoteStatsOptionBoard

data class PerspectiveVoteOptionUiModel(
    val optionId: Long,
    val title: String,
    val imageUrl: String,
    val ratio: Float
)

fun VoteStatsOptionBoard.toUiModel() = PerspectiveVoteOptionUiModel(
    optionId = optionId,
    title = title,
    imageUrl = imageUrl,
    ratio = ratio
)