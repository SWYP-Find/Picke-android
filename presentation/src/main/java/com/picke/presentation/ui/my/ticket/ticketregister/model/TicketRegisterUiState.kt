package com.picke.presentation.ui.my.ticket.ticketregister.model

import com.picke.presentation.ui.my.ticket.model.TicketUiModel

data class TicketRegisterUiState(
    val step: TicketRegisterStep = TicketRegisterStep.CODE,
    val code: String = "",
    val ticket: TicketUiModel? = null,
    val dialog: TicketRegisterDialog? = null,
    val isRequesting: Boolean = false
) {
    val isCodeCheckEnabled: Boolean
        get() = !isRequesting && code.isNotBlank()

    internal val isRegisterEnabled: Boolean
        get() = !isRequesting && ticket != null
}