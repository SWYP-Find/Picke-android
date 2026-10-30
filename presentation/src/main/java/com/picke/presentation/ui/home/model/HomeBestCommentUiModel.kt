package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.HomeBestCommentBoard

data class HomeBestCommentUiModel(
    val battleId: Long,
    val battleTitle: String,
    val optionTitle: String,
    val content: String
)

fun HomeBestCommentBoard.toUiModel(): HomeBestCommentUiModel =
    HomeBestCommentUiModel(
        battleId = battleId,
        battleTitle = battleTitle,
        optionTitle = optionTitle,
        content = content
    )