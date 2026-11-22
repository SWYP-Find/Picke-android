package com.picke.data.feature.proposal.model

import com.picke.domain.feature.proposal.model.ProposalBoard
import kotlinx.serialization.Serializable

// 서버로 보낼 때 (Request)
@Serializable
data class ProposalRequestDto(
    val category: String,
    val topic: String,
    val positionA: String,
    val positionB: String,
    val description: String
)

// 서버에서 받을 때 (Response)
@Serializable
data class ProposalResponseDto(
    val id: Long,
    val userId: Long,
    val nickname: String?,
    val category: String,
    val topic: String,
    val positionA: String,
    val positionB: String,
    val description: String?,
    val status: String,
    val createdAt: String
)

// DTO -> Domain 매퍼
fun ProposalResponseDto.toDomainModel() = ProposalBoard(
    id = id,
    userId = userId,
    nickname = nickname,
    category = category,
    topic = topic,
    positionA = positionA,
    positionB = positionB,
    description = description,
    status = status,
    createdAt = createdAt.take(10)
)