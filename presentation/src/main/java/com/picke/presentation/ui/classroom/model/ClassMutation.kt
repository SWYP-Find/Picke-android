package com.picke.presentation.ui.classroom.model

internal sealed interface ClassMutation {

    sealed interface RequestFinished : ClassMutation

    data object RequestFailed : RequestFinished

    data class TicketStatusLoaded(val ticketStatus: ClassTicketStatus) : RequestFinished
}