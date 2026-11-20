package com.picke.presentation.ui.classroom.myclass

import com.picke.presentation.ui.classroom.myclass.model.MyClassMutation
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiAction
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiState

internal fun reduce(state: MyClassUiState, action: MyClassUiAction): MyClassUiState =
    when (action) {
        is MyClassUiAction.SideEffectOnly -> state

        is MyClassUiAction.FilterSelect -> state.copy(selectedFilter = action.filter)

        is MyClassUiAction.MoreClick -> state.copy(managingClassId = action.classId)

        MyClassUiAction.ManageEditClick -> state.copy(managingClassId = null)

        MyClassUiAction.ManageShareCodeClick -> state.copy(managingClassId = null)

        MyClassUiAction.ManageDeleteClick -> state.copy(isDeleteDialogVisible = true)

        MyClassUiAction.ManageLeaveClick -> state.copy(isLeaveDialogVisible = true)

        MyClassUiAction.ManageSheetDismiss -> state.copy(managingClassId = null)

        MyClassUiAction.DeleteConfirm -> state.copy(
            managingClassId = null,
            isDeleteDialogVisible = false,
            isRequesting = true
        )

        MyClassUiAction.DeleteDialogDismiss -> state.copy(
            managingClassId = null,
            isDeleteDialogVisible = false
        )

        MyClassUiAction.LeaveConfirm -> state.copy(
            managingClassId = null,
            isLeaveDialogVisible = false,
            isRequesting = true
        )

        MyClassUiAction.LeaveDialogDismiss -> state.copy(
            managingClassId = null,
            isLeaveDialogVisible = false
        )
    }

internal fun reduce(state: MyClassUiState, mutation: MyClassMutation): MyClassUiState {
    val reduced = when (mutation) {
        MyClassMutation.RequestFailed -> state

        is MyClassMutation.ClassesLoaded -> state.copy(
            isLoading = false,
            classes = mutation.classes
        )

        MyClassMutation.ClassesLoadFailed -> state.copy(isLoading = false)

        is MyClassMutation.ClassDeleted -> state.copy(classes = state.classes.filterNot { it.id == mutation.classId })

        is MyClassMutation.ClassLeft -> state.copy(classes = state.classes.filterNot { it.id == mutation.classId })
    }

    return if (mutation is MyClassMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}