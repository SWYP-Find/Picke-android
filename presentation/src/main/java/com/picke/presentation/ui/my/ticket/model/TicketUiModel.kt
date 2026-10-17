package com.picke.presentation.ui.my.ticket.model

import java.time.LocalDate

data class TicketUiModel(
    val ticketId: Long,
    val name: String,
    val description: String,
    val maxMemberCount: Int,
    val durationMonths: Int,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)