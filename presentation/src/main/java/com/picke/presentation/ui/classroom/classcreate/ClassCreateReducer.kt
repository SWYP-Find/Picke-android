package com.picke.presentation.ui.classroom.classcreate

import com.picke.presentation.ui.classroom.classcreate.model.ClassCategory
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateErrorDialog
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMutation
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState

private const val MAX_CATEGORY_COUNT = 3

internal fun reduce(state: ClassCreateUiState, action: ClassCreateUiAction): ClassCreateUiState =
    when (action) {
        is ClassCreateUiAction.SideEffectOnly -> state

        ClassCreateUiAction.BackClick -> state.copy(step = state.previousStep ?: state.step)

        ClassCreateUiAction.NextClick -> when {
            state.isTopicTooShort -> state.copy(isTopicInvalid = true)

            !state.isNextEnabled -> state

            state.isMethodChanged -> {
                val defaults = ClassCreateUiState()
                state.copy(
                    step = ClassCreateStep.CONDITION,
                    conditionMethod = state.method,
                    topic = defaults.topic,
                    isTopicInvalid = defaults.isTopicInvalid,
                    selectedLevel = defaults.selectedLevel,
                    selectedCategories = defaults.selectedCategories
                )
            }

            else -> state.copy(
                step = state.nextStep ?: state.step,
                battles = if (state.isNextSelection) emptyList() else state.battles,
                isRecommendationEmpty = false,
                isRequesting = state.nextStep == null || state.isNextSelection
            )
        }

        is ClassCreateUiAction.MethodSelect -> state.copy(method = action.method)

        is ClassCreateUiAction.GuideClick -> state.copy(guideMethod = action.method)

        is ClassCreateUiAction.TopicChange -> state.copy(
            topic = action.topic,
            isTopicInvalid = false
        )

        is ClassCreateUiAction.LevelSelect -> state.copy(selectedLevel = action.level)

        is ClassCreateUiAction.CategorySelect -> state.copy(
            selectedCategories = state.selectedCategories.toggle(action.category)
        )

        ClassCreateUiAction.EditConditionClick -> state.copy(step = ClassCreateStep.CONDITION)

        ClassCreateUiAction.RegenerateClick -> if (state.isRegenerateEnabled) {
            state.copy(
                isRecommendationEmpty = false,
                selectedBattleId = null,
                selectedCandidateId = null,
                isRequesting = true
            )
        } else {
            state
        }

        is ClassCreateUiAction.BattleSelect -> state.copy(
            selectedBattleId = action.battle.battleId,
            selectedCandidateId = action.battle.candidateId
        )

        is ClassCreateUiAction.ClassNameChange -> state.copy(
            className = action.name,
            isClassNameInvalid = false
        )

        is ClassCreateUiAction.DeadlineToggle -> state.copy(
            isDeadlineEnabled = action.isEnabled,
            isDeadlineOutOfRange = false
        )

        ClassCreateUiAction.DeadlineFieldClick -> state.copy(isDeadlineSheetVisible = state.isDeadlineEnabled)

        is ClassCreateUiAction.CommentRequiredToggle -> state.copy(isCommentRequired = action.isRequired)

        is ClassCreateUiAction.OperatorJoinToggle -> state.copy(isOperatorJoin = action.isJoined)

        ClassCreateUiAction.GuideSheetDismiss -> state.copy(guideMethod = null)

        is ClassCreateUiAction.DeadlineConfirm -> state.copy(
            deadline = action.deadline,
            isDeadlineSheetVisible = false,
            isDeadlineOutOfRange = false
        )

        ClassCreateUiAction.DeadlineSheetDismiss -> state.copy(isDeadlineSheetVisible = false)

        ClassCreateUiAction.ErrorDialogDismiss -> state.copy(
            errorDialog = null,
            clarificationMessage = null
        )
    }

internal fun reduce(state: ClassCreateUiState, mutation: ClassCreateMutation): ClassCreateUiState =
    when (mutation) {
        is ClassCreateMutation.TicketEndDateLoaded -> state.copy(ticketEndDate = mutation.ticketEndDate)

        is ClassCreateMutation.BattlesLoaded -> state.copy(
            battles = mutation.battles,
            isRecommendationEmpty = mutation.battles.isEmpty(),
            selectedBattleId = null,
            isRequesting = false
        )

        is ClassCreateMutation.SettingInvalid -> state.copy(
            isClassNameInvalid = mutation.isClassNameInvalid,
            isDeadlineOutOfRange = mutation.isDeadlineOutOfRange,
            isRequesting = false
        )

        is ClassCreateMutation.TopicDraftCreated -> state.copy(
            topicDraftId = mutation.draftId,
            topicBatchNo = 0,
            remainingSuggestions = mutation.remainingSuggestions
        )

        is ClassCreateMutation.TopicsSuggested -> state.copy(
            battles = mutation.battles,
            isRecommendationEmpty = mutation.battles.isEmpty(),
            topicBatchNo = mutation.batchNo,
            remainingSuggestions = mutation.remainingSuggestions,
            selectedCandidateId = null,
            isRequesting = false
        )

        is ClassCreateMutation.TopicClarificationNeeded -> state.copy(
            step = ClassCreateStep.CONDITION,
            remainingSuggestions = mutation.remainingSuggestions,
            errorDialog = ClassCreateErrorDialog.TOPIC_CLARIFICATION_NEEDED,
            clarificationMessage = mutation.message?.ifBlank { null },
            isRequesting = false
        )

        ClassCreateMutation.TopicSuggestLimitReached -> state.copy(
            remainingSuggestions = 0,
            errorDialog = ClassCreateErrorDialog.TOPIC_GENERATION_FAILED,
            isRequesting = false
        )

        is ClassCreateMutation.DeadlineReset -> state.copy(
            deadline = mutation.deadline,
            isDeadlineOutOfRange = false
        )

        is ClassCreateMutation.RequestFailed -> state.copy(
            step = if (state.isSelectionEmpty) ClassCreateStep.CONDITION else state.step,
            errorDialog = mutation.errorDialog,
            isRequesting = false
        )
    }

private fun List<ClassCategory>.toggle(category: ClassCategory): List<ClassCategory> =
    when {
        category in this -> this - category
        size < MAX_CATEGORY_COUNT -> this + category
        else -> this
    }