package com.picke.presentation.ui.classroom.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard

enum class ClassTicketStatus {
    NONE,
    UNREGISTERED,
    ACTIVE,
    EXPIRED
}

enum class ClassTicketDialog {
    NO_PERMISSION,
    UNREGISTERED,
    EXPIRED
}

internal fun ClassTicketBoard.toClassTicketStatus(): ClassTicketStatus =
    if (status == "ACTIVE") ClassTicketStatus.ACTIVE else ClassTicketStatus.EXPIRED