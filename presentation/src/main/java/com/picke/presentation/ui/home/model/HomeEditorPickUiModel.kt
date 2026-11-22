package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.HomeEditorPickBoard
import com.picke.presentation.util.formatCount

data class HomeEditorPickUiModel(
    val battleId: Long,
    val thumbnailUrl: String,
    val optionATitle: String,
    val optionBTitle: String,
    val title: String,
    val summary: String,
    val tags: List<String>,
    val viewCountText: String
)

fun HomeEditorPickBoard.toUiModel(): HomeEditorPickUiModel =
    HomeEditorPickUiModel(
        battleId = battleId,
        thumbnailUrl = thumbnailUrl,
        optionATitle = optionATitle,
        optionBTitle = optionBTitle,
        title = title,
        summary = summary,
        tags = tags,
        viewCountText = formatCount(viewCount)
    )