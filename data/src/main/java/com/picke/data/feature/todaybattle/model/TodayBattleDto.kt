package com.picke.data.feature.todaybattle.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.todaybattle.model.TodayBattleBoard
import com.picke.domain.feature.todaybattle.model.TodayBattleItem
import com.picke.domain.feature.todaybattle.model.TodayBattleOption
import kotlinx.serialization.Serializable

@Serializable
data class TodayBattleResponseDto(
    val items: List<TodayBattleItemDto>,
    val totalCount: Int
)

@Serializable
data class TodayBattleItemDto(
    val battleId: Long,
    val title: String,
    val summary: String?,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val participantsCount: Long,
    val audioDuration: Int,
    val tags: List<TodayBattleTagDto>,
    val options: List<TodayBattleOptionDto>
)

@Serializable
data class TodayBattleTagDto(
    val tagId: Long,
    val name: String,
    val type: String
)

@Serializable
data class TodayBattleOptionDto(
    val optionId: Long,
    val title: String,
    val representative: String?,
    val stance: String?,
    val imageUrl: String?
)

fun TodayBattleResponseDto.toDomainModel(): TodayBattleBoard =
    TodayBattleBoard(
        items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
        totalCount = totalCount
    )

private fun TodayBattleItemDto.toDomainModel(): TodayBattleItem =
    TodayBattleItem(
        battleId = battleId.toString(),
        title = title,
        summary = summary.requireField("TodayBattleItemDto.summary"),
        thumbnailUrl = thumbnailUrl.requireField("TodayBattleItemDto.thumbnailUrl"),
        viewCount = viewCount,
        participantsCount = participantsCount,
        audioDuration = audioDuration,
        tags = tags.map { it.name },
        options = options.map { it.toDomainModel() }
    )

private fun TodayBattleOptionDto.toDomainModel(): TodayBattleOption =
    TodayBattleOption(
        optionId = optionId.toString(),
        title = title,
        representative = representative.requireField("TodayBattleOptionDto.representative"),
        stance = stance.requireField("TodayBattleOptionDto.stance"),
        imageUrl = imageUrl
    )