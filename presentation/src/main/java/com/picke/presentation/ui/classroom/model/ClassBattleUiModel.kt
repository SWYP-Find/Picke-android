package com.picke.presentation.ui.classroom.model

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
    val isAiQuestion: Boolean = false
)