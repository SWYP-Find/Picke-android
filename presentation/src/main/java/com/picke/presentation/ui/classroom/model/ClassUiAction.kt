package com.picke.presentation.ui.classroom.model

sealed interface ClassUiAction {

    sealed interface StateOnly : ClassUiAction

    sealed interface SideEffectOnly : ClassUiAction

    data object BackClick : SideEffectOnly

    data object JoinClick : SideEffectOnly

    data object MyClassClick : SideEffectOnly

    data object CreateClick : SideEffectOnly

    data object TicketClick : SideEffectOnly

    data object TicketDialogRegisterClick : ClassUiAction

    data object TicketDialogDismiss : StateOnly
}