package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationOptionBoard

data class ClassBattleRecommendationResponseDto(
    val items: List<ClassBattleRecommendationItemDto>?
)

data class ClassBattleRecommendationItemDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val thumbnailUrl: String?,
    val category: String?,
    val difficulty: String?,
    val audioDuration: Int?,
    val options: List<ClassBattleRecommendationOptionDto>?
)

data class ClassBattleRecommendationOptionDto(
    val position: String?,
    val label: String?,
    val rationale: String?,
    val philosopher: String?,
    val philosopherName: String?,
    val philosopherImageKey: String?
)

fun ClassBattleRecommendationItemDto.toDomainModel() = ClassBattleRecommendationBoard(
    battleId = battleId ?: 0L,
    title = title ?: "",
    summary = summary ?: "",
    thumbnailUrl = thumbnailUrl ?: "",
    category = category ?: "",
    difficulty = difficulty ?: "",
    audioDuration = audioDuration ?: 0,
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassBattleRecommendationOptionDto.toDomainModel() = ClassBattleRecommendationOptionBoard(
    position = position ?: "",
    label = label ?: "",
    rationale = rationale ?: "",
    philosopher = philosopher ?: "",
    philosopherName = philosopherName ?: "",
    philosopherImageKey = philosopherImageKey ?: ""
)