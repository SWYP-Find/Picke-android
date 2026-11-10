package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.HomeBestBattleBoard
import com.picke.presentation.util.formatCount

data class HomeBestBattleUiModel(
    val battleId: Long,
    val philosopherA: String,
    val philosopherB: String,
    val title: String,
    val tags: List<String>,
    val timeInfoText: String,
    val viewCountText: String
)

fun HomeBestBattleBoard.toUiModel(): HomeBestBattleUiModel =
    HomeBestBattleUiModel(
        battleId = battleId,
        philosopherA = philosopherA,
        philosopherB = philosopherB,
        title = title,
        tags = tags,
        timeInfoText = "${audioDuration / 60}분",
        viewCountText = formatCount(viewCount)
    )