package com.picke.presentation.ui.my.ticket

import com.picke.presentation.ui.my.ticket.model.TicketMutation
import com.picke.presentation.ui.my.ticket.model.TicketUiState

internal fun reduce(state: TicketUiState, mutation: TicketMutation): TicketUiState =
    when (mutation) {
        is TicketMutation.TicketsLoaded -> state.copy(
            isLoading = false,
            tickets = mutation.tickets
        )
    }