package com.picke.data.feature.home.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.home.model.HomeBestBattleBoard
import com.picke.domain.feature.home.model.HomeBoard
import com.picke.domain.feature.home.model.HomeEditorPickBoard
import com.picke.domain.feature.home.model.HomeNewBattleBoard
import com.picke.domain.feature.home.model.HomeTrendingBattleBoard
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(
    val newNotice: Boolean,
    val editorPicks: List<EditorPickDto>,
    val trendingBattles: List<TrendingBattleDto>,
    val bestBattles: List<BestBattleDto>,
    val newBattles: List<NewBattleDto>
)

@Serializable
data class TagDto(
    val tagId: Long,
    val name: String,
    val type: String
)

@Serializable
data class EditorPickDto(
    val battleId: Long,
    val thumbnailUrl: String?,
    val optionATitle: String?,
    val optionBTitle: String?,
    val title: String,
    val summary: String?,
    val tags: List<TagDto>,
    val viewCount: Int,
    val audioDuration: Int
)

@Serializable
data class TrendingBattleDto(
    val badgeLabel: String,
    val battleId: Long,
    val thumbnailUrl: String?,
    val title: String,
    val tags: List<TagDto>,
    val audioDuration: Int,
    val viewCount: Int
)

@Serializable
data class BestBattleDto(
    val badgeLabel: String,
    val battleId: Long,
    val philosopherA: String?,
    val philosopherB: String?,
    val title: String,
    val tags: List<TagDto>,
    val audioDuration: Int,
    val viewCount: Int
)

@Serializable
data class NewBattleDto(
    val battleId: Long,
    val thumbnailUrl: String?,
    val title: String,
    val summary: String?,
    val philosopherA: String?,
    val optionATitle: String?,
    val philosopherAImageUrl: String?,
    val philosopherB: String?,
    val optionBTitle: String?,
    val philosopherBImageUrl: String?,
    val tags: List<TagDto>,
    val audioDuration: Int,
    val viewCount: Int
)

fun HomeResponseDto.toDomainModel(): HomeBoard =
    HomeBoard(
        newNotice = newNotice,
        editorPicks = editorPicks.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
        trendingBattles = trendingBattles.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
        bestBattles = bestBattles.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
        newBattles = newBattles.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } }
    )

private fun EditorPickDto.toDomainModel(): HomeEditorPickBoard =
    HomeEditorPickBoard(
        battleId = battleId,
        thumbnailUrl = thumbnailUrl.requireField("EditorPickDto.thumbnailUrl"),
        optionATitle = optionATitle.requireField("EditorPickDto.optionATitle"),
        optionBTitle = optionBTitle.requireField("EditorPickDto.optionBTitle"),
        title = title,
        summary = summary.requireField("EditorPickDto.summary"),
        tags = tags.map { it.name },
        viewCount = viewCount,
        audioDuration = audioDuration
    )

private fun TrendingBattleDto.toDomainModel(): HomeTrendingBattleBoard =
    HomeTrendingBattleBoard(
        badgeLabel = badgeLabel,
        battleId = battleId,
        thumbnailUrl = thumbnailUrl.requireField("TrendingBattleDto.thumbnailUrl"),
        title = title,
        tags = tags.map { it.name },
        audioDuration = audioDuration,
        viewCount = viewCount
    )

private fun BestBattleDto.toDomainModel(): HomeBestBattleBoard =
    HomeBestBattleBoard(
        badgeLabel = badgeLabel,
        battleId = battleId,
        philosopherA = philosopherA.requireField("BestBattleDto.philosopherA"),
        philosopherB = philosopherB.requireField("BestBattleDto.philosopherB"),
        title = title,
        tags = tags.map { it.name },
        audioDuration = audioDuration,
        viewCount = viewCount
    )

private fun NewBattleDto.toDomainModel(): HomeNewBattleBoard =
    HomeNewBattleBoard(
        battleId = battleId,
        thumbnailUrl = thumbnailUrl,
        title = title,
        summary = summary.requireField("NewBattleDto.summary"),
        philosopherA = philosopherA.requireField("NewBattleDto.philosopherA"),
        optionATitle = optionATitle.requireField("NewBattleDto.optionATitle"),
        philosopherAImageUrl = philosopherAImageUrl,
        philosopherB = philosopherB.requireField("NewBattleDto.philosopherB"),
        optionBTitle = optionBTitle.requireField("NewBattleDto.optionBTitle"),
        philosopherBImageUrl = philosopherBImageUrl,
        tags = tags.map { it.name },
        audioDuration = audioDuration,
        viewCount = viewCount
    )