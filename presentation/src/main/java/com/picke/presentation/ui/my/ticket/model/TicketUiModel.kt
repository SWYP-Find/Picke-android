package com.picke.presentation.ui.my.ticket.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.presentation.util.parseServerDateTime
import java.time.LocalDate

data class TicketUiModel(
    val ticketId: Long,
    val maxMemberCount: Int,
    val durationMonths: Int,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

fun ClassTicketBoard.toUiModel() = TicketUiModel(
    ticketId = ticketId,
    maxMemberCount = capacity,
    durationMonths = 0,
    startDate = parseServerDateTime(activatedAt)?.toLocalDate(),
    endDate = parseServerDateTime(expiresAt)?.toLocalDate()
)