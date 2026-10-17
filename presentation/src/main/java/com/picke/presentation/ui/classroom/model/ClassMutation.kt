package com.picke.presentation.ui.classroom.model

internal sealed interface ClassMutation {
    data class TicketStatusLoaded(val ticketStatus: ClassTicketStatus) : ClassMutation
}