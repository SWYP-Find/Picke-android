package com.picke.presentation.ui.my.ticket.ticketregistercomplete.model

import com.picke.presentation.ui.my.ticket.model.TicketUiModel

data class TicketRegisterCompleteUiState(
    val ticketId: Long = 0L,
    val ticket: TicketUiModel? = null
)