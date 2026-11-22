package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassMemberBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassJoinRequestDto(
    val inviteCode: String,
    val displayName: String
)

@Serializable
data class ClassJoinPreviewResponseDto(
    val inviteCode: String,
    val name: String,
    val battleTitle: String,
    val battleType: String,
    val memberCount: Int,
    val capacity: Int,
    val closeAt: String?
)

@Serializable
data class ClassMemberNameUpdateRequestDto(
    val displayName: String
)

@Serializable
data class ClassMemberResponseDto(
    val memberId: Long,
    val displayName: String,
    val role: String,
    val characterUrl: String?,
    val me: Boolean,
    val joinedAt: String
)

fun ClassJoinPreviewResponseDto.toDomainModel() = ClassJoinPreviewBoard(
    inviteCode = inviteCode,
    name = name,
    battleTitle = battleTitle,
    battleType = battleType,
    memberCount = memberCount,
    capacity = capacity,
    closeAt = closeAt.requireField("ClassJoinPreviewResponseDto.closeAt")
)

fun ClassMemberResponseDto.toDomainModel() = ClassMemberBoard(
    memberId = memberId,
    displayName = displayName,
    role = role,
    characterUrl = characterUrl,
    me = me,
    joinedAt = joinedAt
)