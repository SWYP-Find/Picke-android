package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.MyClassBoard
import kotlinx.serialization.Serializable

@Serializable
data class MyClassResponseDto(
    val classId: Long,
    val name: String,
    val myRole: String?,
    val battleId: Long,
    val battleTitle: String,
    val battleThumbnailUrl: String?,
    val battleType: String,
    val memberCount: Int,
    val capacity: Int,
    val closeAt: String?,
    val closed: Boolean,
    val createdAt: String
)

@Serializable
data class ClassCreateRequestDto(
    val name: String,
    val battleId: Long?,
    val topicVersionId: Long?,
    val deadline: String?,
    val commentRequired: Boolean,
    val ownerParticipates: Boolean
)

@Serializable
data class ClassUpdateRequestDto(
    val name: String?,
    val deadline: String?
)

@Serializable
data class ClassDetailResponseDto(
    val classId: Long,
    val name: String,
    val inviteCode: String,
    val myRole: String,
    val battle: ClassBattleInfoDto,
    val battleType: String,
    val memberCount: Int,
    val capacity: Int,
    val deadline: String?,
    val ticketExpiresAt: String?,
    val closeAt: String?,
    val closed: Boolean,
    val commentRequired: Boolean,
    val ownerParticipates: Boolean
)

@Serializable
data class ClassBattleInfoDto(
    val battleId: Long
)

fun MyClassResponseDto.toDomainModel() = MyClassBoard(
    classId = classId,
    name = name,
    myRole = myRole.requireField("MyClassResponseDto.myRole"),
    battleId = battleId,
    battleTitle = battleTitle,
    battleThumbnailUrl = battleThumbnailUrl,
    battleType = battleType,
    memberCount = memberCount,
    capacity = capacity,
    closeAt = closeAt.requireField("MyClassResponseDto.closeAt"),
    closed = closed,
    createdAt = createdAt
)

fun ClassDetailResponseDto.toDomainModel() = ClassDetailBoard(
    classId = classId,
    name = name,
    inviteCode = inviteCode,
    myRole = myRole,
    battleId = battle.battleId,
    battleType = battleType,
    memberCount = memberCount,
    capacity = capacity,
    deadline = deadline,
    ticketExpiresAt = ticketExpiresAt.requireField("ClassDetailResponseDto.ticketExpiresAt"),
    closeAt = closeAt.requireField("ClassDetailResponseDto.closeAt"),
    closed = closed,
    commentRequired = commentRequired,
    ownerParticipates = ownerParticipates
)