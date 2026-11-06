package com.picke.data.feature.todaybattle.model

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
    val summary: String,
    val thumbnailUrl: String,
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
    val representative: String,
    val stance: String,
    val imageUrl: String?
)

// DTO -> Domain Model
fun TodayBattleResponseDto.toDomainModel(): TodayBattleBoard{
    return TodayBattleBoard(
        items = items.map { it.toDomainModel() },
        totalCount = totalCount
    )
}

fun TodayBattleItemDto.toDomainModel(): TodayBattleItem {
    return TodayBattleItem(
        battleId = battleId.toString(),
        title = title,
        summary = summary,
        thumbnailUrl = thumbnailUrl ?: "",
        type = "",
        viewCount = viewCount,
        participantsCount = participantsCount,
        audioDuration = audioDuration,
        tags = tags.map { it.name },
        options = options.map { it.toDomainModel() }
    )
}

fun TodayBattleOptionDto.toDomainModel(): TodayBattleOption {
    return TodayBattleOption(
        optionId = optionId.toString(),
        title = title,
        representative = representative ?: "",
        stance = stance,
        imageUrl = imageUrl
    )
}