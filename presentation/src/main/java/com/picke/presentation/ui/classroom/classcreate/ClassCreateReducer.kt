package com.picke.presentation.ui.classroom.classcreate

import com.picke.presentation.ui.classroom.classcreate.model.ClassCategory
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMutation
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState

private const val MAX_CATEGORY_COUNT = 3

internal fun reduce(state: ClassCreateUiState, action: ClassCreateUiAction): ClassCreateUiState =
    when (action) {
        ClassCreateUiAction.BackClick -> state.copy(step = state.previousStep ?: state.step)
        ClassCreateUiAction.NextClick -> state.copy(step = state.nextStep ?: state.step)
        ClassCreateUiAction.EditConditionClick -> state.copy(step = ClassCreateStep.CONDITION)

        is ClassCreateUiAction.MethodSelect -> state.copy(method = action.method)
        is ClassCreateUiAction.GuideClick -> state.copy(guideMethod = action.method)
        ClassCreateUiAction.GuideSheetDismiss -> state.copy(guideMethod = null)
        is ClassCreateUiAction.TopicChange -> state.copy(topic = action.topic)
        is ClassCreateUiAction.LevelSelect -> state.copy(selectedLevel = action.level)
        is ClassCreateUiAction.CategorySelect -> state.copy(selectedCategories = state.selectedCategories.toggle(action.category))

        is ClassCreateUiAction.BattleSelect -> state.copy(selectedBattleId = action.battleId)
        is ClassCreateUiAction.BattlePreviewClick -> state
        ClassCreateUiAction.RegenerateClick -> state.copy(selectedBattleId = null)

        is ClassCreateUiAction.ClassNameChange -> state.copy(className = action.name)
        is ClassCreateUiAction.DeadlineToggle -> state.copy(isDeadlineEnabled = action.isEnabled)
        ClassCreateUiAction.DeadlineFieldClick -> state.copy(isDeadlineSheetVisible = state.isDeadlineEnabled)
        is ClassCreateUiAction.DeadlineConfirm -> state.copy(deadline = action.deadline, isDeadlineSheetVisible = false)
        ClassCreateUiAction.DeadlineSheetDismiss -> state.copy(isDeadlineSheetVisible = false)
        is ClassCreateUiAction.CommentRequiredToggle -> state.copy(isCommentRequired = action.isRequired)
        is ClassCreateUiAction.OperatorJoinToggle -> state.copy(isOperatorJoin = action.isJoined)
    }

internal fun reduce(state: ClassCreateUiState, mutation: ClassCreateMutation): ClassCreateUiState =
    when (mutation) {
        is ClassCreateMutation.BattlesLoaded -> state.copy(
            battles = mutation.battles,
            selectedBattleId = null
        )

        is ClassCreateMutation.DeadlineReset -> state.copy(deadline = mutation.deadline)
    }

private fun List<ClassCategory>.toggle(category: ClassCategory): List<ClassCategory> =
    when {
        category in this -> this - category
        size < MAX_CATEGORY_COUNT -> this + category
        else -> this
    }