package com.picke.presentation.ui.explore.model

import com.picke.domain.feature.explore.model.ExploreItemBoard
import com.picke.presentation.util.formatCount

data class ExploreUiModel(
    val battleId: String,
    val thumbnailUrl: String,
    val title: String,
    val summary: String,
    val tags: List<String>,
    val audioDurationText: String,
    val viewCountText: String
)

fun ExploreItemBoard.toUiModel(): ExploreUiModel {
    return ExploreUiModel(
        battleId = battleId.toString(),
        thumbnailUrl = thumbnailUrl,
        title = title,
        summary = summary,
        tags = tags.map { it.name },
        audioDurationText = "${audioDuration / 60}분",
        viewCountText = formatCount(viewCount)
    )
}