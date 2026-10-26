package com.picke.presentation.ui.classroom.model

import com.picke.domain.feature.battle.model.BattleOptionBoard
import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationOptionBoard
import com.picke.domain.feature.classroom.model.ClassTopicCandidateBoard
import com.picke.domain.feature.classroom.model.ClassTopicOptionBoard

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
    val commentCount: Int = 0,
    val candidateId: Long? = null
)

fun ClassBattleDetailBoard.toUiModel(battleType: ClassBattleType): ClassBattleUiModel {
    val battleInfo = battle.battleInfo
    val isAiQuestion = battleType == ClassBattleType.CUSTOM
    val categoryName = battle.categoryTags.firstOrNull()?.name
    val durationMinutes = (battleInfo.audioDuration + SECONDS_PER_MINUTE - 1) / SECONDS_PER_MINUTE
    return ClassBattleUiModel(
        battleId = battleInfo.battleId.toLongOrNull() ?: 0L,
        tag = if (categoryName != null) "#$categoryName" else "",
        title = battleInfo.title,
        description = battleInfo.summary,
        durationMinutes = if (isAiQuestion || durationMinutes == 0) null else durationMinutes,
        options = battleInfo.options.map { it.toClassBattleOptionUiModel() },
        isAiQuestion = isAiQuestion
    )
}

fun ClassBattleRecommendationBoard.toUiModel(): ClassBattleUiModel {
    val durationMinutes = (audioDuration + SECONDS_PER_MINUTE - 1) / SECONDS_PER_MINUTE
    return ClassBattleUiModel(
        battleId = battleId,
        tag = if (category.isNotBlank()) "#$category" else "",
        title = title,
        description = summary,
        durationMinutes = if (durationMinutes == 0) null else durationMinutes,
        options = options.map { it.toClassBattleOptionUiModel() }
    )
}

fun ClassTopicCandidateBoard.toUiModel() = ClassBattleUiModel(
    battleId = 0L,
    tag = if (category.isNotBlank()) "#$category" else "",
    title = question,
    description = description,
    options = options.map { it.toClassBattleOptionUiModel() },
    isAiQuestion = true,
    candidateId = candidateId
)

private fun BattleOptionBoard.toClassBattleOptionUiModel() = ClassBattleOptionUiModel(
    stance = title,
    subText = representative,
    imageUrl = imageUrl.ifBlank { null }
)

private fun ClassBattleRecommendationOptionBoard.toClassBattleOptionUiModel() = ClassBattleOptionUiModel(
    stance = label,
    subText = philosopherName
)

private fun ClassTopicOptionBoard.toClassBattleOptionUiModel() = ClassBattleOptionUiModel(
    stance = label,
    subText = philosopherName
)