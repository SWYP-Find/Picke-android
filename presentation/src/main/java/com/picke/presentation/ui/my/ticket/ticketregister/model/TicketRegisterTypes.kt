package com.picke.presentation.ui.my.ticket.ticketregister.model

enum class TicketRegisterStep {
    CODE,
    CONFIRM
}

enum class TicketCodeGuide {
    CODE_LOCATION,
    JOIN_CODE
}

enum class TicketRegisterDialog {
    ALREADY_REGISTERED,
    INVALID_CODE,
    REGISTER_FAILED,
    EXPIRED
}