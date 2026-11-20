package com.picke.data.feature.explore.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.explore.model.ExploreItemBoard
import com.picke.domain.feature.explore.model.ExplorePageBoard
import com.picke.domain.feature.explore.model.ExploreTagBoard
import kotlinx.serialization.Serializable

@Serializable
data class ExploreTagDto(
    val tagId: Long,
    val name: String,
    val type: String
)

// [응답 DTO] 탐색/검색 아이템 단건
@Serializable
data class ExploreItemDto(
    val battleId: Long,
    val thumbnailUrl: String?,
    val title: String,
    val summary: String?,
    val tags: List<ExploreTagDto>,
    val audioDuration: Int?,
    val viewCount: Int?
)

// [응답 DTO] 탐색 리스트 (페이지네이션)
@Serializable
data class ExplorePageResponseDto(
    val items: List<ExploreItemDto>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

// DTO -> Domain
fun ExploreTagDto.toDomainModel() = ExploreTagBoard(
    tagId = tagId,
    name = name,
    type = type
)

fun ExploreItemDto.toDomainModel() = ExploreItemBoard(
    battleId = battleId,
    thumbnailUrl = thumbnailUrl.requireField("ExploreItemDto.thumbnailUrl"),
    title = title,
    summary = summary.requireField("ExploreItemDto.summary"),
    tags = tags.map { it.toDomainModel() },
    audioDuration = audioDuration.requireField("ExploreItemDto.audioDuration"),
    viewCount = viewCount.requireField("ExploreItemDto.viewCount")
)

fun ExplorePageResponseDto.toDomainModel() = ExplorePageBoard(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextOffset = nextOffset,
    hasNext = hasNext
)