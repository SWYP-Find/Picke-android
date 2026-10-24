package com.picke.presentation.ui.classroom.classcreate

import com.picke.presentation.ui.classroom.classcreate.model.ClassCategory
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMutation
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState

private const val MAX_CATEGORY_COUNT = 3

internal fun reduce(state: ClassCreateUiState, action: ClassCreateUiAction): ClassCreateUiState =
    when (action) {
        is ClassCreateUiAction.SideEffectOnly -> state

        ClassCreateUiAction.BackClick -> state.copy(step = state.previousStep ?: state.step)

        ClassCreateUiAction.NextClick -> if (state.isNextEnabled) {
            state.copy(
                step = state.nextStep ?: state.step,
                isRecommendationEmpty = false,
                isRequesting = state.nextStep == null || state.nextStep == ClassCreateStep.SELECTION
            )
        } else {
            state
        }

        is ClassCreateUiAction.MethodSelect -> state.copy(method = action.method)

        is ClassCreateUiAction.GuideClick -> state.copy(guideMethod = action.method)

        is ClassCreateUiAction.TopicChange -> state.copy(topic = action.topic)

        is ClassCreateUiAction.LevelSelect -> state.copy(selectedLevel = action.level)

        is ClassCreateUiAction.CategorySelect -> state.copy(
            selectedCategories = state.selectedCategories.toggle(action.category)
        )

        ClassCreateUiAction.EditConditionClick -> state.copy(step = ClassCreateStep.CONDITION)

        ClassCreateUiAction.RegenerateClick -> if (state.isRequesting) {
            state
        } else {
            state.copy(
                isRecommendationEmpty = false,
                selectedBattleId = null,
                isRequesting = true
            )
        }

        is ClassCreateUiAction.BattleSelect -> state.copy(selectedBattleId = action.battleId)

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

        ClassCreateUiAction.ErrorDialogDismiss -> state.copy(errorDialog = null)
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

        is ClassCreateMutation.DeadlineReset -> state.copy(
            deadline = mutation.deadline,
            isDeadlineOutOfRange = false
        )

        is ClassCreateMutation.RequestFailed -> state.copy(
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