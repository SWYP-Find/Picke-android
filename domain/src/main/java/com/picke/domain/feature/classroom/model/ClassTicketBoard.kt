package com.picke.domain.feature.classroom.model

data class ClassTicketBoard(
    val ticketId: Long,
    val capacity: Int,
    val status: String,
    val activatedAt: String,
    val expiresAt: String,
    val remainingDays: Int,
    val readGraceEndsAt: String
)

data class ClassTicketPreviewBoard(
    val capacity: Int,
    val registerBy: String,
    val usageMonths: Int,
    val registeredByMe: Boolean
)