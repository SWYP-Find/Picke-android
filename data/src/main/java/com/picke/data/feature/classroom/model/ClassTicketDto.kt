package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassTicketBoard

data class ClassTicketResponseDto(
    val ticketId: Long?,
    val capacity: Int?,
    val status: String?,
    val activatedAt: String?,
    val expiresAt: String?,
    val remainingDays: Int?,
    val readGraceEndsAt: String?
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