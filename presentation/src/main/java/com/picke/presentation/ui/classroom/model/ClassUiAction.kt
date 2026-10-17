package com.picke.presentation.ui.classroom.model

sealed interface ClassUiAction {

    sealed interface StateOnly : ClassUiAction

    sealed interface SideEffectOnly : ClassUiAction

    data object BackClick : SideEffectOnly

    data object JoinClick : SideEffectOnly

    data object MyClassClick : SideEffectOnly

    data object CreateClick : ClassUiAction

    data object TicketClick : SideEffectOnly

    data object CreateDialogRegisterClick : ClassUiAction

    data object CreateDialogDismiss : StateOnly
}