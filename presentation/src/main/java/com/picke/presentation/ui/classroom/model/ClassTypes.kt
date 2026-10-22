package com.picke.presentation.ui.classroom.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard

enum class ClassTicketStatus {
    NONE,
    UNREGISTERED,
    ACTIVE,
    EXPIRED
}

enum class ClassRole {
    OWNER,
    MEMBER;

    companion object {
        fun from(value: String): ClassRole = entries.find { it.name == value } ?: MEMBER
    }
}

enum class ClassTicketDialog {
    NO_PERMISSION,
    UNREGISTERED,
    EXPIRED
}

internal fun ClassTicketBoard.toClassTicketStatus(): ClassTicketStatus =
    if (status == "ACTIVE") ClassTicketStatus.ACTIVE else ClassTicketStatus.EXPIRED