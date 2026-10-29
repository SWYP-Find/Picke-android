package com.picke.presentation.ui.my.ticket.model

sealed interface TicketUiEvent {

    data object NavigateBack : TicketUiEvent

    data object NavigateToRegister : TicketUiEvent

    data object ReplaceWithRegister : TicketUiEvent

    data object NavigateBackAfterLoadFailed : TicketUiEvent

    data object ShowLoadFailedToast : TicketUiEvent
}