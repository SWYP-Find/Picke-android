package com.picke.presentation.ui.classroom.classdetail.model

sealed interface ClassDetailUiAction {
    sealed interface StateOnly : ClassDetailUiAction

    sealed interface SideEffectOnly : ClassDetailUiAction

    data object BackClick : SideEffectOnly

    data object MoreClick : StateOnly

    data object MemberClick : SideEffectOnly

    data object DeadlineEditClick : SideEffectOnly

    data object BattleClick : SideEffectOnly

    data object ResultClick : SideEffectOnly

    data object ManageEditClick : ClassDetailUiAction

    data object ManageShareCodeClick : ClassDetailUiAction

    data object ManageDeleteClick : StateOnly

    data object ManageLeaveClick : StateOnly

    data object ManageSheetDismiss : StateOnly

    data object DeleteConfirm : ClassDetailUiAction

    data object DeleteDialogDismiss : StateOnly

    data object LeaveConfirm : ClassDetailUiAction

    data object LeaveDialogDismiss : StateOnly
}