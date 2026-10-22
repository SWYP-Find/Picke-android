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
    ticketId = this.ticketId ?: 0L,
    capacity = this.capacity ?: 0,
    status = this.status ?: "",
    activatedAt = this.activatedAt ?: "",
    expiresAt = this.expiresAt ?: "",
    remainingDays = this.remainingDays ?: 0,
    readGraceEndsAt = this.readGraceEndsAt ?: ""
)

fun ClassTicketPreviewResponseDto.toDomainModel() = ClassTicketPreviewBoard(
    capacity = this.capacity ?: 0,
    registerBy = this.registerBy ?: "",
    usageMonths = this.usageMonths ?: 0,
    registeredByMe = this.registeredByMe ?: false
)