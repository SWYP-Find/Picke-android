package com.picke.presentation.ui.home.model

import com.picke.domain.feature.perspective.model.WeeklyBestPerspectiveItemBoard

data class HomeBestCommentUiModel(
    val battleId: Long,
    val battleTitle: String,
    val content: String
)

fun WeeklyBestPerspectiveItemBoard.toUiModel(): HomeBestCommentUiModel =
    HomeBestCommentUiModel(
        battleId = battleId,
        battleTitle = battleTitle,
        content = content
    )