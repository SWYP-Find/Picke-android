package com.picke.data.feature.recommend.model

import com.picke.domain.feature.recommend.model.RecommendBoard
import com.picke.domain.feature.recommend.model.RecommendOptionBoard
import com.picke.domain.feature.recommend.model.RecommendPageBoard
import com.picke.domain.feature.recommend.model.RecommendTagBoard
import kotlinx.serialization.Serializable

@Serializable
data class RecommendTagDto(
    val tagId: Long?,
    val name: String?
)

@Serializable
data class RecommendOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?,
    val representative: String?,
    val imageUrl: String?
)

@Serializable
data class RecommendationItemDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val audioDuration: Int?,
    val viewCount: Int?,
    val tags: List<RecommendTagDto>?,
    val participantsCount: Int?,
    val options: List<RecommendOptionDto>?
)

@Serializable
data class RecommendationPageResponseDto(
    val items: List<RecommendationItemDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

fun RecommendTagDto.toDomainModel() = RecommendTagBoard(
    tagId = tagId ?: 0L,
    name = name ?: ""
)

fun RecommendOptionDto.toDomainModel() = RecommendOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: "",
    stance = stance ?: "",
    representative = representative ?: "",
    imageUrl = imageUrl ?: ""
)

fun RecommendationItemDto.toDomainModel() = RecommendBoard(
    battleId = battleId ?: 0L,
    title = title ?: "",
    summary = summary ?: "",
    audioDuration = audioDuration ?: 0,
    viewCount = viewCount ?: 0,
    tags = tags?.map { it.toDomainModel() } ?: emptyList(),
    participantsCount = participantsCount ?: 0,
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun RecommendationPageResponseDto.toDomainModel() = RecommendPageBoard(
    items = items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = nextCursor,
    hasNext = hasNext ?: false
)