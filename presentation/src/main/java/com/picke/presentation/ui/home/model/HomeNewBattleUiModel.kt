package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.HomeNewBattleBoard
import com.picke.presentation.util.formatCount

data class HomeNewBattleUiModel(
    val battleId: Long,
    val title: String,
    val summary: String,
    val philosopherA: String,
    val optionATitle: String,
    val philosopherAImageUrl: String?,
    val philosopherB: String,
    val optionBTitle: String,
    val philosopherBImageUrl: String?,
    val tags: List<String>,
    val timeInfoText: String,
    val viewCountText: String
)

fun HomeNewBattleBoard.toUiModel(): HomeNewBattleUiModel =
    HomeNewBattleUiModel(
        battleId = battleId,
        title = title,
        summary = summary,
        philosopherA = philosopherA,
        optionATitle = optionATitle,
        philosopherAImageUrl = philosopherAImageUrl,
        philosopherB = philosopherB,
        optionBTitle = optionBTitle,
        philosopherBImageUrl = philosopherBImageUrl,
        tags = tags,
        timeInfoText = "${audioDuration / 60}분",
        viewCountText = formatCount(viewCount)
    )