package com.picke.presentation.ui.my.ticket.ticketregistercomplete.model

sealed interface TicketRegisterCompleteUiAction {

    data object BackClick : TicketRegisterCompleteUiAction

    data object TicketCheckClick : TicketRegisterCompleteUiAction
}