package com.picke.presentation.ui.my.ticket.model

data class TicketUiState(
    val isLoading: Boolean = true,
    val tickets: List<TicketUiModel> = emptyList()
)