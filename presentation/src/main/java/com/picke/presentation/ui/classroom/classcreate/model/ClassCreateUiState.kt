package com.picke.presentation.ui.classroom.classcreate.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

data class ClassCreateUiState(
    val step: ClassCreateStep = ClassCreateStep.METHOD,
    val method: ClassCreateMethod = ClassCreateMethod.CONTENT,
    val guideMethod: ClassCreateMethod? = null,
    val topic: String = "",
    val selectedLevel: ClassLevel = ClassLevel.BEGINNER,
    val selectedCategories: List<ClassCategory> = listOf(ClassCategory.PHILOSOPHY),
    val battles: List<ClassBattleUiModel> = emptyList(),
    val selectedBattleId: Long? = null,
    val className: String = "",
    val isDeadlineEnabled: Boolean = true,
    val deadline: LocalDateTime? = null,
    val isDeadlineSheetVisible: Boolean = false,
    val isCommentRequired: Boolean = false,
    val isOperatorJoin: Boolean = false
) {
    val selectedBattle: ClassBattleUiModel?
        get() = battles.firstOrNull { it.battleId == selectedBattleId }

    val isNextEnabled: Boolean
        get() = when (step) {
            ClassCreateStep.METHOD -> true
            ClassCreateStep.CONDITION -> selectedCategories.isNotEmpty()
            ClassCreateStep.SELECTION -> selectedBattleId != null
            ClassCreateStep.SETTING -> className.isNotBlank()
        }

    internal val nextStep: ClassCreateStep?
        get() = steps.getOrNull(steps.indexOf(step) + 1)

    internal val previousStep: ClassCreateStep?
        get() = steps.getOrNull(steps.indexOf(step) - 1)

    private val steps: List<ClassCreateStep>
        get() = if (method == ClassCreateMethod.AI_TOPIC) ClassCreateStep.entries - ClassCreateStep.CONDITION else ClassCreateStep.entries
}