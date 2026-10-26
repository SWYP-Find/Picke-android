package com.picke.presentation.ui.classroom.classcreate.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDate
import java.time.LocalDateTime

private const val MIN_AI_TOPIC_LENGTH = 2

data class ClassCreateUiState(
    val step: ClassCreateStep = ClassCreateStep.METHOD,
    val method: ClassCreateMethod = ClassCreateMethod.CONTENT,
    val guideMethod: ClassCreateMethod? = null,
    val topic: String = "",
    val isTopicInvalid: Boolean = false,
    val selectedLevel: ClassLevel = ClassLevel.BEGINNER,
    val selectedCategories: List<ClassCategory> = listOf(ClassCategory.PHILOSOPHY),
    val battles: List<ClassBattleUiModel> = emptyList(),
    val isRecommendationEmpty: Boolean = false,
    val selectedBattleId: Long? = null,
    val topicDraftId: Long? = null,
    val topicBatchNo: Int = 0,
    val remainingSuggestions: Int = 0,
    val selectedCandidateId: Long? = null,
    val className: String = "",
    val isClassNameInvalid: Boolean = false,
    val isDeadlineEnabled: Boolean = true,
    val deadline: LocalDateTime? = null,
    val isDeadlineSheetVisible: Boolean = false,
    val isDeadlineOutOfRange: Boolean = false,
    val ticketEndDate: LocalDate? = null,
    val isCommentRequired: Boolean = false,
    val isOperatorJoin: Boolean = false,
    val errorDialog: ClassCreateErrorDialog? = null,
    val clarificationMessage: String? = null,
    val isRequesting: Boolean = false
) {
    val selectedBattle: ClassBattleUiModel?
        get() = if (method == ClassCreateMethod.AI_TOPIC) {
            battles.firstOrNull { it.candidateId == selectedCandidateId }
        } else {
            battles.firstOrNull { it.battleId == selectedBattleId }
        }

    val isNextEnabled: Boolean
        get() = !isRequesting && when (step) {
            ClassCreateStep.METHOD, ClassCreateStep.SETTING -> true
            ClassCreateStep.CONDITION -> selectedCategories.isNotEmpty()
            ClassCreateStep.SELECTION -> selectedBattle != null
        }

    val isRegenerateEnabled: Boolean
        get() = !isRequesting && remainingSuggestions > 0

    val isTopicRequired: Boolean
        get() = method == ClassCreateMethod.AI_TOPIC

    internal val isTopicTooShort: Boolean
        get() = step == ClassCreateStep.CONDITION && isTopicRequired && topic.trim().length < MIN_AI_TOPIC_LENGTH

    internal val nextStep: ClassCreateStep?
        get() = ClassCreateStep.entries.getOrNull(step.ordinal + 1)

    internal val previousStep: ClassCreateStep?
        get() = ClassCreateStep.entries.getOrNull(step.ordinal - 1)
}