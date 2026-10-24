package com.picke.domain.feature.classroom.model

data class ClassBattleRecommendationBoard(
    val battleId: Long,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val category: String,
    val difficulty: String,
    val audioDuration: Int,
    val options: List<ClassBattleRecommendationOptionBoard>
)

data class ClassBattleRecommendationOptionBoard(
    val position: String,
    val label: String,
    val rationale: String,
    val philosopher: String,
    val philosopherName: String,
    val philosopherImageKey: String
)