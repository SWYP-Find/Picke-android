package com.picke.presentation.ui.my.ticket.ticketregistercomplete

import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteMutation
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiState

internal fun reduce(
    state: TicketRegisterCompleteUiState,
    mutation: TicketRegisterCompleteMutation
): TicketRegisterCompleteUiState =
    when (mutation) {
        is TicketRegisterCompleteMutation.TicketLoaded -> state.copy(ticket = mutation.ticket)
    }