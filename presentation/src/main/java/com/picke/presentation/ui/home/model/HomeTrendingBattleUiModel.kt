package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.HomeTrendingBattleBoard
import com.picke.presentation.util.formatCount

data class HomeTrendingBattleUiModel(
    val battleId: Long,
    val thumbnailUrl: String,
    val title: String,
    val tags: List<String>,
    val timeInfoText: String,
    val viewCountText: String
)

fun HomeTrendingBattleBoard.toUiModel(): HomeTrendingBattleUiModel =
    HomeTrendingBattleUiModel(
        battleId = battleId,
        thumbnailUrl = thumbnailUrl,
        title = title,
        tags = tags,
        timeInfoText = "${audioDuration / 60}분",
        viewCountText = formatCount(viewCount)
    )