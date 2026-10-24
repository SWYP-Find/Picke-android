package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard

data class ClassTicketRegisterRequestDto(
    val code: String
)

data class ClassTicketResponseDto(
    val ticketId: Long?,
    val capacity: Int?,
    val status: String?,
    val activatedAt: String?,
    val expiresAt: String?,
    val remainingDays: Int?,
    val readGraceEndsAt: String?
)

data class ClassTicketPreviewResponseDto(
    val capacity: Int?,
    val registerBy: String?,
    val usageMonths: Int?,
    val registeredByMe: Boolean?
)

fun ClassTicketResponseDto.toDomainModel() = ClassTicketBoard(
    ticketId = ticketId ?: 0L,
    capacity = capacity ?: 0,
    status = status ?: "",
    activatedAt = activatedAt ?: "",
    expiresAt = expiresAt ?: "",
    remainingDays = remainingDays ?: 0,
    readGraceEndsAt = readGraceEndsAt ?: ""
)

fun ClassTicketPreviewResponseDto.toDomainModel() = ClassTicketPreviewBoard(
    capacity = capacity ?: 0,
    registerBy = registerBy ?: "",
    usageMonths = usageMonths ?: 0,
    registeredByMe = registeredByMe ?: false
)