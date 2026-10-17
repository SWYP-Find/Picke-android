package com.picke.presentation.ui.my.ticket.model

sealed interface TicketUiAction {

    data object BackClick : TicketUiAction

    data object AddRegisterClick : TicketUiAction
}