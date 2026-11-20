package com.picke.domain.feature.classroom.model

data class ClassBattleRecommendationBoard(
    val battleId: Long,
    val title: String,
    val summary: String,
    val thumbnailUrl: String?,
    val category: String,
    val difficulty: String,
    val audioDuration: Int?,
    val options: List<ClassBattleRecommendationOptionBoard>
)

data class ClassBattleRecommendationOptionBoard(
    val optionId: Long,
    val position: String?,
    val title: String,
    val stance: String?,
    val representative: String,
    val imageUrl: String?
)