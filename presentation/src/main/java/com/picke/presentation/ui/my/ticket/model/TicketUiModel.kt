package com.picke.presentation.ui.my.ticket.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.presentation.util.parseServerDateTime
import java.time.LocalDate

data class TicketUiModel(
    val maxMemberCount: Int,
    val durationMonths: Int,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

fun ClassTicketBoard.toUiModel() = TicketUiModel(
    maxMemberCount = capacity,
    durationMonths = 0,
    startDate = parseServerDateTime(activatedAt)?.toLocalDate(),
    endDate = parseServerDateTime(expiresAt)?.toLocalDate()
)

fun ClassTicketPreviewBoard.toUiModel() = TicketUiModel(
    maxMemberCount = capacity,
    durationMonths = usageMonths
)