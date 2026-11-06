package com.picke.data.feature.perspective.model

import com.picke.domain.feature.perspective.model.PerspectiveBoard
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeCountBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeToggleBoard
import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.model.PerspectiveStatusBoard
import com.picke.domain.feature.perspective.model.PerspectiveUpdateBoard
import kotlinx.serialization.Serializable

// [공통 DTO]
@Serializable
data class PerspectiveRequestDto(
    val content: String
)

@Serializable
data class PerspectiveUserDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String?,
    val characterImageUrl: String?
)

@Serializable
data class PerspectiveOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?
)

// [API 1: 관점 리스트 조회]
@Serializable
data class PerspectivePageDto(
    val items: List<PerspectiveDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

@Serializable
data class PerspectiveDto(
    val perspectiveId: Long?,
    val user: PerspectiveUserDto?,
    val option: PerspectiveOptionDto?,
    val content: String?,
    val likeCount: Int?,
    val commentCount: Int?,
    val isLiked: Boolean?,
    val isMyPerspective: Boolean?,
    val createdAt: String?
)

// [API 2 & 4 & 5 & 6 & 7 유지보수용 DTO]
@Serializable
data class PerspectiveCreateResponseDto(
    val perspectiveId: Long?,
    val status: String?,
    val createdAt: String?
)

// [API 3: 내 관점 조회 & API 4: 관점 단건 조회 공통 DTO]
@Serializable
data class PerspectiveDetailResponseDto(
    val perspectiveId: Long?,
    val user: PerspectiveUserDto?,
    val option: PerspectiveOptionDto?,
    val content: String?,
    val likeCount: Int?,
    val commentCount: Int?,
    val isLiked: Boolean?,
    val status: String?,
    val isMyPerspective: Boolean?,
    val createdAt: String?
)

// [API 8: 관점 좋아요 수 조회 응답 DTO]
@Serializable
data class PerspectiveLikeCountResponseDto(
    val perspectiveId: Long?,
    val likeCount: Int?
)

// [API 9 & 10: 관점 좋아요 등록 및 취소 응답 DTO]
@Serializable
data class PerspectiveLikeToggleResponseDto(
    val perspectiveId: Long?,
    val likeCount: Int?,
    val isLiked: Boolean?
)

@Serializable
data class PerspectiveUpdateResponseDto(
    val perspectiveId: Long?,
    val content: String?,
    val updatedAt: String?
)

// ==========================================
// DTO -> Domain Mappers
// ==========================================

fun PerspectivePageDto.toDomainModel() = PerspectivePage(
    items = items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = nextCursor,
    hasNext = hasNext ?: false
)

fun PerspectiveDto.toDomainModel() = PerspectiveBoard(
    perspectiveId = perspectiveId ?: 0L,
    nickname = user?.nickname ?: "알 수 없음",
    characterType = user?.characterType ?: "",
    characterImageUrl = user?.characterImageUrl ?: "",
    content = content ?: "",
    isMine = isMyPerspective ?: false,
    createdAt = createdAt ?: "",
    optionTitle = option?.title ?: "",
    optionId = option?.optionId ?: 0L,
    replyCount = commentCount ?: 0,
    likeCount = likeCount ?: 0,
    isLiked = isLiked ?: false
)

fun PerspectiveCreateResponseDto.toDomainModel() = PerspectiveStatusBoard(
    perspectiveId = perspectiveId ?: 0L,
    status = status ?: "UNKNOWN",
    createdAt = createdAt ?: ""
)

// 내 관점 단건, 일반 단건 매퍼 통합
fun PerspectiveDetailResponseDto.toDomainModel() = PerspectiveDetailBoard(
    perspectiveId = perspectiveId ?: 0L,
    userTag = user?.userTag ?: "",
    nickname = user?.nickname ?: "알 수 없음",
    characterImageUrl = user?.characterImageUrl ?: "",
    optionTitle = option?.title ?: "",
    optionId = option?.optionId ?: 0L,
    content = content ?: "",
    likeCount = likeCount ?: 0,
    commentCount = commentCount ?: 0,
    isLiked = isLiked ?: false,
    status = status ?: "PUBLISHED",
    isMine = isMyPerspective ?: true,
    createdAt = createdAt ?: ""
)

fun PerspectiveUpdateResponseDto.toDomainModel() = PerspectiveUpdateBoard(
    perspectiveId = perspectiveId ?: 0L,
    content = content ?: "",
    updatedAt = updatedAt ?: ""
)

fun PerspectiveLikeCountResponseDto.toDomainModel() = PerspectiveLikeCountBoard(
    perspectiveId = perspectiveId ?: 0L,
    likeCount = likeCount ?: 0
)

fun PerspectiveLikeToggleResponseDto.toDomainModel() = PerspectiveLikeToggleBoard(
    perspectiveId = perspectiveId ?: 0L,
    likeCount = likeCount ?: 0,
    isLiked = isLiked ?: false
)