package com.picke.presentation.ui.classroom.classmember.model

sealed interface ClassMemberUiAction {
    sealed interface StateOnly : ClassMemberUiAction

    sealed interface SideEffectOnly : ClassMemberUiAction

    data object BackClick : SideEffectOnly

    data class QueryChange(val query: String) : StateOnly

    data object EditNameClick : StateOnly

    data class EditingNameChange(val name: String) : StateOnly

    data object NameConfirm : ClassMemberUiAction

    data object NameSheetDismiss : StateOnly

    data class RemoveClick(val member: ClassMemberUiModel) : StateOnly

    data object RemoveConfirm : ClassMemberUiAction

    data object RemoveDialogDismiss : StateOnly
}