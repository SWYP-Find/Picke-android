package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationOptionBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassBattleRecommendationResponseDto(
    val items: List<ClassBattleRecommendationItemDto>
)

@Serializable
data class ClassBattleRecommendationItemDto(
    val battleId: Long,
    val title: String,
    val summary: String?,
    val thumbnailUrl: String?,
    val category: String,
    val difficulty: String,
    val audioDuration: Int?,
    val options: List<ClassBattleRecommendationOptionDto>
)

@Serializable
data class ClassBattleRecommendationOptionDto(
    val optionId: Long,
    val position: String?,
    val title: String,
    val stance: String?,
    val representative: String?,
    val imageUrl: String?
)

fun ClassBattleRecommendationItemDto.toDomainModel() = ClassBattleRecommendationBoard(
    battleId = battleId,
    title = title,
    summary = summary.requireField("ClassBattleRecommendationItemDto.summary"),
    thumbnailUrl = thumbnailUrl,
    category = category,
    difficulty = difficulty,
    audioDuration = audioDuration,
    options = options.map { it.toDomainModel() }
)

fun ClassBattleRecommendationOptionDto.toDomainModel() = ClassBattleRecommendationOptionBoard(
    optionId = optionId,
    position = position,
    title = title,
    stance = stance,
    representative = representative.requireField("ClassBattleRecommendationOptionDto.representative"),
    imageUrl = imageUrl
)