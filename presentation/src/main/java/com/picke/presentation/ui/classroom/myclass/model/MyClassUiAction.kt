package com.picke.presentation.ui.classroom.myclass.model

sealed interface MyClassUiAction {
    sealed interface StateOnly : MyClassUiAction

    data object BackClick : MyClassUiAction
    data class FilterSelect(val filter: MyClassFilter) : StateOnly
    data class ClassClick(val classId: Long) : MyClassUiAction

    data class MoreClick(val classId: Long) : StateOnly
    data object ManageEditClick : MyClassUiAction
    data object ManageShareCodeClick : MyClassUiAction
    data object ManageDeleteClick : StateOnly
    data object ManageLeaveClick : StateOnly
    data object ManageSheetDismiss : StateOnly

    data object DeleteConfirm : MyClassUiAction
    data object DeleteDialogDismiss : StateOnly

    data object LeaveConfirm : MyClassUiAction
    data object LeaveDialogDismiss : StateOnly
}