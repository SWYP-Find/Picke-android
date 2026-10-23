package com.picke.presentation.ui.classroom.classdetail

import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailMutation
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiAction
import com.picke.presentation.ui.classroom.classdetail.model.ClassDetailUiState

internal fun reduce(state: ClassDetailUiState, action: ClassDetailUiAction): ClassDetailUiState =
    when (action) {
        is ClassDetailUiAction.SideEffectOnly -> state

        ClassDetailUiAction.MoreClick -> state.copy(isManageSheetVisible = true)
        ClassDetailUiAction.ManageEditClick -> state.copy(isManageSheetVisible = false)
        ClassDetailUiAction.ManageShareCodeClick -> state.copy(isManageSheetVisible = false)
        ClassDetailUiAction.ManageDeleteClick -> state.copy(isManageSheetVisible = false, isDeleteDialogVisible = true)
        ClassDetailUiAction.ManageLeaveClick -> state.copy(isManageSheetVisible = false, isLeaveDialogVisible = true)
        ClassDetailUiAction.ManageSheetDismiss -> state.copy(isManageSheetVisible = false)

        ClassDetailUiAction.DeleteConfirm -> state.copy(isDeleteDialogVisible = false)
        ClassDetailUiAction.DeleteDialogDismiss -> state.copy(isDeleteDialogVisible = false)

        ClassDetailUiAction.LeaveConfirm -> state.copy(isLeaveDialogVisible = false)
        ClassDetailUiAction.LeaveDialogDismiss -> state.copy(isLeaveDialogVisible = false)
    }

internal fun reduce(state: ClassDetailUiState, mutation: ClassDetailMutation): ClassDetailUiState =
    when (mutation) {
        is ClassDetailMutation.ClassDetailLoaded -> state.copy(
            isOperator = mutation.isOperator,
            isInProgress = mutation.isInProgress,
            title = mutation.title,
            memberCount = mutation.memberCount,
            deadline = mutation.deadline
        )

        is ClassDetailMutation.BattleLoaded -> state.copy(battle = mutation.battle)
    }