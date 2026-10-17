package com.picke.presentation.ui.my.ticket.model

internal sealed interface TicketMutation {

    data class TicketsLoaded(val tickets: List<TicketUiModel>) : TicketMutation
}