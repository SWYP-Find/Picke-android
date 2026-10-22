package com.picke.presentation.ui.my.ticket.ticketregister.model

sealed interface TicketRegisterUiEvent {

    data object NavigateBack : TicketRegisterUiEvent

    data class NavigateToRegisterComplete(val ticketId: Long) : TicketRegisterUiEvent

    data object NavigateToTicket : TicketRegisterUiEvent
}