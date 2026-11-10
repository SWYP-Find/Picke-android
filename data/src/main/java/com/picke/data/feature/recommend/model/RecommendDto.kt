package com.picke.data.feature.recommend.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.recommend.model.RecommendBoard
import com.picke.domain.feature.recommend.model.RecommendOptionBoard
import com.picke.domain.feature.recommend.model.RecommendPageBoard
import com.picke.domain.feature.recommend.model.RecommendTagBoard
import kotlinx.serialization.Serializable

@Serializable
data class RecommendTagDto(
    val tagId: Long,
    val name: String
)

@Serializable
data class RecommendOptionDto(
    val optionId: Long,
    val title: String,
    val stance: String?,
    val representative: String?,
    val imageUrl: String
)

@Serializable
data class RecommendationItemDto(
    val battleId: Long,
    val title: String,
    val summary: String?,
    val audioDuration: Int?,
    val viewCount: Int?,
    val tags: List<RecommendTagDto>,
    val participantsCount: Int,
    val options: List<RecommendOptionDto>
)

@Serializable
data class RecommendationPageResponseDto(
    val items: List<RecommendationItemDto>,
    val nextCursor: String?,
    val hasNext: Boolean
)

fun RecommendTagDto.toDomainModel() = RecommendTagBoard(
    tagId = tagId,
    name = name
)

fun RecommendOptionDto.toDomainModel() = RecommendOptionBoard(
    optionId = optionId,
    title = title,
    stance = stance.requireField("RecommendOptionDto.stance"),
    representative = representative.requireField("RecommendOptionDto.representative"),
    imageUrl = imageUrl
)

fun RecommendationItemDto.toDomainModel() = RecommendBoard(
    battleId = battleId,
    title = title,
    summary = summary.requireField("RecommendationItemDto.summary"),
    audioDuration = audioDuration.requireField("RecommendationItemDto.audioDuration"),
    viewCount = viewCount.requireField("RecommendationItemDto.viewCount"),
    tags = tags.map { it.toDomainModel() },
    participantsCount = participantsCount,
    options = options.map { it.toDomainModel() }
)

fun RecommendationPageResponseDto.toDomainModel() = RecommendPageBoard(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextCursor = nextCursor,
    hasNext = hasNext
)