package com.picke.presentation.ui.my.ticket.ticketregister

import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterMutation
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterStep
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiAction
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiState

internal fun reduce(
    state: TicketRegisterUiState,
    action: TicketRegisterUiAction
): TicketRegisterUiState =
    when (action) {
        is TicketRegisterUiAction.SideEffectOnly -> state

        TicketRegisterUiAction.BackClick -> if (state.step == TicketRegisterStep.CONFIRM) {
            state.copy(step = TicketRegisterStep.CODE)
        } else {
            state
        }

        is TicketRegisterUiAction.CodeChange ->
            state.copy(code = action.code.toTicketCode())

        TicketRegisterUiAction.DialogBackClick -> state.copy(dialog = null)

        TicketRegisterUiAction.DialogDismiss -> state.copy(dialog = null)
    }

internal fun reduce(
    state: TicketRegisterUiState,
    mutation: TicketRegisterMutation
): TicketRegisterUiState =
    when (mutation) {
        is TicketRegisterMutation.CodeVerified -> state.copy(
            step = TicketRegisterStep.CONFIRM,
            ticket = mutation.ticket
        )
    }

private fun String.toTicketCode(): String =
    uppercase().filter { it in 'A'..'Z' || it in '0'..'9' || it == '-' }