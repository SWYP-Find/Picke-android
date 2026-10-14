package com.picke.presentation.ui.classroom.myclass

import com.picke.presentation.ui.classroom.myclass.model.MyClassMutation
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiAction
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiState

internal fun reduce(state: MyClassUiState, action: MyClassUiAction): MyClassUiState =
    when (action) {
        MyClassUiAction.BackClick -> state
        is MyClassUiAction.FilterSelect -> state.copy(selectedFilter = action.filter)
        is MyClassUiAction.ClassClick -> state

        is MyClassUiAction.MoreClick -> state.copy(managingClassId = action.classId)
        MyClassUiAction.ManageEditClick -> state.copy(managingClassId = null)
        MyClassUiAction.ManageShareCodeClick -> state.copy(managingClassId = null)
        MyClassUiAction.ManageDeleteClick -> state.copy(isDeleteDialogVisible = true)
        MyClassUiAction.ManageLeaveClick -> state.copy(isLeaveDialogVisible = true)
        MyClassUiAction.ManageSheetDismiss -> state.copy(managingClassId = null)

        MyClassUiAction.DeleteConfirm -> state.copy(
            managingClassId = null,
            isDeleteDialogVisible = false
        )

        MyClassUiAction.DeleteDialogDismiss -> state.copy(
            managingClassId = null,
            isDeleteDialogVisible = false
        )

        MyClassUiAction.LeaveConfirm -> state.copy(
            managingClassId = null,
            isLeaveDialogVisible = false
        )

        MyClassUiAction.LeaveDialogDismiss -> state.copy(
            managingClassId = null,
            isLeaveDialogVisible = false
        )
    }

internal fun reduce(state: MyClassUiState, mutation: MyClassMutation): MyClassUiState =
    when (mutation) {
        is MyClassMutation.ClassesLoaded -> state.copy(classes = mutation.classes)
        is MyClassMutation.ClassLeft -> state.copy(classes = state.classes.filterNot { it.id == mutation.classId })
        is MyClassMutation.ClassDeleted -> state.copy(classes = state.classes.filterNot { it.id == mutation.classId })
    }