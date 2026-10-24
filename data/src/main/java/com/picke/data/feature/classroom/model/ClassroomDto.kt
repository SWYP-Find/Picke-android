package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.MyClassBoard

data class MyClassResponseDto(
    val classId: Long?,
    val name: String?,
    val myRole: String?,
    val battleId: Long?,
    val battleTitle: String?,
    val battleThumbnailUrl: String?,
    val battleType: String?,
    val memberCount: Int?,
    val capacity: Int?,
    val closeAt: String?,
    val closed: Boolean?,
    val createdAt: String?
)

data class ClassCreateRequestDto(
    val name: String,
    val battleId: Long,
    val deadline: String?,
    val commentRequired: Boolean,
    val ownerParticipates: Boolean
)

data class ClassDetailResponseDto(
    val classId: Long?,
    val name: String?,
    val inviteCode: String?,
    val myRole: String?,
    val battle: ClassBattleInfoDto?,
    val battleType: String?,
    val memberCount: Int?,
    val capacity: Int?,
    val closeAt: String?,
    val closed: Boolean?,
    val commentRequired: Boolean?,
    val ownerParticipates: Boolean?
)

data class ClassBattleInfoDto(
    val battleId: Long?
)

fun MyClassResponseDto.toDomainModel() = MyClassBoard(
    classId = classId ?: 0L,
    name = name ?: "",
    myRole = myRole ?: "",
    battleId = battleId ?: 0L,
    battleTitle = battleTitle ?: "",
    battleThumbnailUrl = battleThumbnailUrl ?: "",
    battleType = battleType ?: "",
    memberCount = memberCount ?: 0,
    capacity = capacity ?: 0,
    closeAt = closeAt ?: "",
    closed = closed ?: false,
    createdAt = createdAt ?: ""
)

fun ClassDetailResponseDto.toDomainModel() = ClassDetailBoard(
    classId = classId ?: 0L,
    name = name ?: "",
    inviteCode = inviteCode ?: "",
    myRole = myRole ?: "",
    battleId = battle?.battleId ?: 0L,
    battleType = battleType ?: "",
    memberCount = memberCount ?: 0,
    capacity = capacity ?: 0,
    closeAt = closeAt ?: "",
    closed = closed ?: false,
    commentRequired = commentRequired ?: false,
    ownerParticipates = ownerParticipates ?: false
)