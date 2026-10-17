package com.picke.presentation.ui.my.ticket.ticketregistercomplete.model

import com.picke.presentation.ui.my.ticket.model.TicketUiModel

internal sealed interface TicketRegisterCompleteMutation {
    data class TicketLoaded(val ticket: TicketUiModel) : TicketRegisterCompleteMutation
}