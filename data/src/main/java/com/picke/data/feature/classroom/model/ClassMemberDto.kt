package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassMemberBoard

data class ClassJoinRequestDto(
    val inviteCode: String,
    val displayName: String
)

data class ClassJoinPreviewResponseDto(
    val inviteCode: String?,
    val name: String?,
    val battleTitle: String?,
    val battleType: String?,
    val memberCount: Int?,
    val capacity: Int?,
    val closeAt: String?
)

data class ClassMemberResponseDto(
    val memberId: Long?,
    val displayName: String?,
    val role: String?,
    val characterUrl: String?,
    val me: Boolean?,
    val joinedAt: String?
)

fun ClassJoinPreviewResponseDto.toDomainModel() = ClassJoinPreviewBoard(
    inviteCode = inviteCode ?: "",
    name = name ?: "",
    battleTitle = battleTitle ?: "",
    battleType = battleType ?: "",
    memberCount = memberCount ?: 0,
    capacity = capacity ?: 0,
    closeAt = closeAt ?: ""
)

fun ClassMemberResponseDto.toDomainModel() = ClassMemberBoard(
    memberId = memberId ?: 0L,
    displayName = displayName ?: "",
    role = role ?: "",
    characterUrl = characterUrl ?: "",
    me = me ?: false,
    joinedAt = joinedAt ?: ""
)