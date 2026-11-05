package com.picke.data.feature.explore.model

import com.picke.domain.feature.explore.model.ExploreItemBoard
import com.picke.domain.feature.explore.model.ExplorePageBoard
import com.picke.domain.feature.explore.model.ExploreTagBoard

data class ExploreTagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

// [응답 DTO] 탐색/검색 아이템 단건
data class ExploreItemDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val type: String?,
    val title: String?,
    val summary: String?,
    val tags: List<ExploreTagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

// [응답 DTO] 탐색 리스트 (페이지네이션)
data class ExplorePageResponseDto(
    val items: List<ExploreItemDto>?,
    val nextOffset: Int?,
    val hasNext: Boolean?
)

// DTO -> Domain
fun ExploreTagDto.toDomainModel() = ExploreTagBoard(
    tagId = tagId ?: 0L,
    name = name ?: "",
    type = type ?: ""
)

fun ExploreItemDto.toDomainModel() = ExploreItemBoard(
    battleId = battleId ?: 0L,
    thumbnailUrl = thumbnailUrl ?: "",
    type = type ?: "BATTLE",
    title = title ?: "",
    summary = summary ?: "",
    tags = tags?.map { it.toDomainModel() } ?: emptyList(),
    audioDuration = audioDuration ?: 0,
    viewCount = viewCount ?: 0
)

fun ExplorePageResponseDto.toDomainModel() = ExplorePageBoard(
    items = items?.map { it.toDomainModel() } ?: emptyList(),
    nextOffset = nextOffset,
    hasNext = hasNext ?: false
)