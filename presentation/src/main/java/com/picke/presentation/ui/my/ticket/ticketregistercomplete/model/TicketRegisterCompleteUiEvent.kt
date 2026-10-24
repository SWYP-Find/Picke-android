package com.picke.presentation.ui.my.ticket.ticketregistercomplete.model

sealed interface TicketRegisterCompleteUiEvent {

    data object NavigateBack : TicketRegisterCompleteUiEvent

    data object NavigateToTicket : TicketRegisterCompleteUiEvent

    data object ShowLoadFailedToast : TicketRegisterCompleteUiEvent
}