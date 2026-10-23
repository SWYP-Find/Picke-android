package com.picke.presentation.ui.classroom.model

import com.picke.domain.feature.explore.model.ExploreItemBoard

private const val SECONDS_PER_MINUTE = 60

data class ClassBattleOptionUiModel(
    val stance: String,
    val subText: String,
    val imageUrl: String? = null
)

data class ClassBattleUiModel(
    val battleId: Long,
    val tag: String,
    val title: String,
    val description: String,
    val durationMinutes: Int? = null,
    val options: List<ClassBattleOptionUiModel> = emptyList(),
    val isAiQuestion: Boolean = false,
    val commentCount: Int = 0
)

fun ExploreItemBoard.toClassBattleUiModel(): ClassBattleUiModel {
    val categoryName = tags.firstOrNull()?.name
    val durationMinutes = (audioDuration + SECONDS_PER_MINUTE - 1) / SECONDS_PER_MINUTE
    return ClassBattleUiModel(
        battleId = battleId,
        tag = if (categoryName != null) "#$categoryName" else "",
        title = title,
        description = summary,
        durationMinutes = if (durationMinutes == 0) null else durationMinutes
    )
}