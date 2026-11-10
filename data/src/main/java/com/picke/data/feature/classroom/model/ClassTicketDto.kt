package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassTicketRegisterRequestDto(
    val code: String
)

@Serializable
data class ClassTicketResponseDto(
    val ticketId: Long,
    val capacity: Int,
    val status: String,
    val activatedAt: String?,
    val expiresAt: String?,
    val remainingDays: Int,
    val readGraceEndsAt: String?
)

@Serializable
data class ClassTicketPreviewResponseDto(
    val capacity: Int,
    val registerBy: String,
    val usageMonths: Int,
    val registeredByMe: Boolean
)

fun ClassTicketResponseDto.toDomainModel() = ClassTicketBoard(
    ticketId = ticketId,
    capacity = capacity,
    status = status,
    activatedAt = activatedAt.requireField("ClassTicketResponseDto.activatedAt"),
    expiresAt = expiresAt.requireField("ClassTicketResponseDto.expiresAt"),
    remainingDays = remainingDays,
    readGraceEndsAt = readGraceEndsAt.requireField("ClassTicketResponseDto.readGraceEndsAt")
)

fun ClassTicketPreviewResponseDto.toDomainModel() = ClassTicketPreviewBoard(
    capacity = capacity,
    registerBy = registerBy,
    usageMonths = usageMonths,
    registeredByMe = registeredByMe
)