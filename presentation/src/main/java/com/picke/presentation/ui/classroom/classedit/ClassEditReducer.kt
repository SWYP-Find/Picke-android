package com.picke.presentation.ui.classroom.classedit

import com.picke.presentation.ui.classroom.classedit.model.ClassEditMutation
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiAction
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiState

internal fun reduce(state: ClassEditUiState, action: ClassEditUiAction): ClassEditUiState =
    when (action) {
        is ClassEditUiAction.SideEffectOnly -> state

        is ClassEditUiAction.ClassNameChange -> state.copy(
            className = action.name,
            isClassNameInvalid = false
        )

        ClassEditUiAction.DeadlineFieldClick -> state.copy(isDeadlineSheetVisible = state.deadline != null)

        ClassEditUiAction.SaveClick -> if (state.isSaveEnabled) state.copy(isRequesting = true) else state

        is ClassEditUiAction.DeadlineConfirm -> state.copy(
            deadline = action.deadline,
            isDeadlineSheetVisible = false,
            isDeadlineOutOfRange = false
        )

        ClassEditUiAction.DeadlineSheetDismiss -> state.copy(isDeadlineSheetVisible = false)
    }

internal fun reduce(state: ClassEditUiState, mutation: ClassEditMutation): ClassEditUiState {
    val reduced = when (mutation) {
        ClassEditMutation.RequestFailed -> state

        is ClassEditMutation.ClassLoaded -> state.copy(
            originalClassName = mutation.className,
            className = mutation.className,
            originalDeadline = mutation.deadline,
            deadline = mutation.deadline,
            ticketEndDate = mutation.ticketEndDate
        )

        is ClassEditMutation.BattleLoaded -> state.copy(battle = mutation.battle)

        is ClassEditMutation.SettingInvalid -> state.copy(
            isClassNameInvalid = mutation.isClassNameInvalid,
            isDeadlineOutOfRange = mutation.isDeadlineOutOfRange
        )
    }
    return if (mutation is ClassEditMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}