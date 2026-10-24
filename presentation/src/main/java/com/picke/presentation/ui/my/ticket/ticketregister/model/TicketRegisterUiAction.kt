package com.picke.presentation.ui.my.ticket.ticketregister.model

sealed interface TicketRegisterUiAction {

    sealed interface StateOnly : TicketRegisterUiAction

    sealed interface SideEffectOnly : TicketRegisterUiAction

    data object BackClick : TicketRegisterUiAction

    data class CodeChange(val code: String) : StateOnly

    data object CodeCheckClick : TicketRegisterUiAction

    data object RegisterClick : TicketRegisterUiAction

    data object DialogBackClick : TicketRegisterUiAction

    data object DialogDismiss : StateOnly
}