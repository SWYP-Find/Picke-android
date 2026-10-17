package com.picke.presentation.ui.classroom.classjoin.model

sealed interface ClassJoinUiAction {
    sealed interface StateOnly : ClassJoinUiAction

    sealed interface SideEffectOnly : ClassJoinUiAction

    data object BackClick : ClassJoinUiAction

    data object NextClick : SideEffectOnly

    data class CodeChange(val code: String) : StateOnly

    data class NameChange(val name: String) : StateOnly

    data object DialogDismiss : StateOnly
}