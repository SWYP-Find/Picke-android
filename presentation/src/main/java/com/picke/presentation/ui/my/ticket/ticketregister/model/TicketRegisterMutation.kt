package com.picke.presentation.ui.my.ticket.ticketregister.model

import com.picke.presentation.ui.my.ticket.model.TicketUiModel

internal sealed interface TicketRegisterMutation {

    sealed interface RequestFinished : TicketRegisterMutation

    data class RequestFailed(val dialog: TicketRegisterDialog) : RequestFinished

    data class CodeVerified(val ticket: TicketUiModel) : RequestFinished
}