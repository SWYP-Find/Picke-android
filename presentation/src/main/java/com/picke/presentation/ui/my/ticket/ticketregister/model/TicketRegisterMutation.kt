package com.picke.presentation.ui.my.ticket.ticketregister.model

import com.picke.presentation.ui.my.ticket.model.TicketUiModel

internal sealed interface TicketRegisterMutation {

    data class CodeVerified(val ticket: TicketUiModel) : TicketRegisterMutation

    data class RequestFailed(val dialog: TicketRegisterDialog) : TicketRegisterMutation
}