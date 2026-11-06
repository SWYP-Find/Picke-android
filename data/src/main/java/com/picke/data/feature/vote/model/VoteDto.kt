package com.picke.data.feature.vote.model

import com.picke.domain.feature.vote.model.MyVoteBoard
import com.picke.domain.feature.vote.model.VoteStatsBoard
import com.picke.domain.feature.vote.model.VoteStatsOptionBoard
import com.picke.domain.feature.vote.model.VotedOptionBoard
import kotlinx.serialization.Serializable

@Serializable
data class VoteRequestDto(
    val optionId: Long
)

// [응답 DTO] 투표 제출 성공
@Serializable
data class VoteResponseDto(
    val voteId: Long?,
    val status: String?
)

// [응답 DTO] 투표 통계 조회
@Serializable
data class VoteStatsDto(
    val options: List<VoteStatsOptionDto>?,
    val totalCount: Int?,
    val updatedAt: String?
)

@Serializable
data class VoteStatsOptionDto(
    val optionId: Long?,
    val title: String?,
    val imageUrl: String?,
    val isCorrect: Boolean?,
    val voteCount: Int?,
    val ratio: Float?,
    val stance: String?
)

// [응답 DTO] 내 투표 내역 조회
@Serializable
data class MyVoteResponseDto(
    val battleTitle: String?,
    val preVote: VotedOptionDto?,
    val postVote: VotedOptionDto?,
    val status: String?,
    val opinionChanged: Boolean?
)

@Serializable
data class VotedOptionDto(
    val optionId: Long?,
    val title: String?
)

// DTO -> Domain

fun VoteStatsOptionDto.toDomainModel() = VoteStatsOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: "",
    imageUrl = imageUrl ?: "",
    isCorrect = isCorrect ?: false,
    voteCount = voteCount ?: 0,
    ratio = ratio ?: 0f,
    stance = stance ?: ""
)

fun VoteStatsDto.toDomainModel() = VoteStatsBoard(
    totalCount = totalCount ?: 0,
    updatedAt = updatedAt ?: "",
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun VotedOptionDto.toDomainModel() = VotedOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: ""
)

fun MyVoteResponseDto.toDomainModel() = MyVoteBoard(
    battleTitle = battleTitle ?: "",
    preVote = preVote?.toDomainModel(),
    postVote = postVote?.toDomainModel(),
    status = status ?: "NONE",
    opinionChanged = opinionChanged ?: false
)