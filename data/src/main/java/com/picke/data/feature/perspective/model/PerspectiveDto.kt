package com.picke.data.feature.perspective.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.perspective.model.PerspectiveBoard
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeCountBoard
import com.picke.domain.feature.perspective.model.PerspectiveLikeToggleBoard
import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.model.PerspectiveStatusBoard
import com.picke.domain.feature.perspective.model.PerspectiveUpdateBoard
import kotlinx.serialization.Serializable

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
    val optionId: Long,
    val label: String?,
    val title: String,
    val stance: String?
)

@Serializable
data class PerspectivePageDto(
    val items: List<PerspectiveDto>?,
    val nextCursor: String?,
    val hasNext: Boolean
)

@Serializable
data class PerspectiveDto(
    val perspectiveId: Long,
    val user: PerspectiveUserDto,
    val option: PerspectiveOptionDto,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isMyPerspective: Boolean,
    val createdAt: String
)

@Serializable
data class PerspectiveDetailResponseDto(
    val perspectiveId: Long,
    val user: PerspectiveUserDto,
    val option: PerspectiveOptionDto,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isMyPerspective: Boolean,
    val createdAt: String
)

@Serializable
data class MyPerspectiveResponseDto(
    val perspectiveId: Long,
    val user: PerspectiveUserDto,
    val option: PerspectiveOptionDto,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val status: String,
    val createdAt: String
)

@Serializable
data class PerspectiveLikeCountResponseDto(
    val perspectiveId: Long,
    val likeCount: Long
)

@Serializable
data class PerspectiveCreateResponseDto(
    val perspectiveId: Long,
    val status: String,
    val createdAt: String
)

@Serializable
data class PerspectiveUpdateResponseDto(
    val perspectiveId: Long,
    val content: String,
    val updatedAt: String
)

@Serializable
data class PerspectiveLikeToggleResponseDto(
    val perspectiveId: Long,
    val likeCount: Int,
    val isLiked: Boolean
)

fun PerspectivePageDto.toDomainModel(): PerspectivePage =
    PerspectivePage(
        items = items?.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } } ?: emptyList(),
        nextCursor = nextCursor,
        hasNext = hasNext
    )

private fun PerspectiveDto.toDomainModel(): PerspectiveBoard =
    PerspectiveBoard(
        perspectiveId = perspectiveId,
        userTag = user.userTag,
        nickname = user.nickname.requireField("PerspectiveUserDto.nickname"),
        characterType = user.characterType,
        characterImageUrl = user.characterImageUrl,
        optionId = option.optionId,
        optionLabel = option.label,
        optionTitle = option.title,
        optionStance = option.stance,
        content = content,
        likeCount = likeCount,
        commentCount = commentCount,
        isLiked = isLiked,
        isMyPerspective = isMyPerspective,
        createdAt = createdAt
    )

fun PerspectiveDetailResponseDto.toDomainModel(): PerspectiveDetailBoard =
    PerspectiveDetailBoard(
        perspectiveId = perspectiveId,
        userTag = user.userTag,
        nickname = user.nickname.requireField("PerspectiveUserDto.nickname"),
        characterType = user.characterType,
        characterImageUrl = user.characterImageUrl,
        optionId = option.optionId,
        optionLabel = option.label,
        optionTitle = option.title,
        optionStance = option.stance,
        content = content,
        likeCount = likeCount,
        commentCount = commentCount,
        isLiked = isLiked,
        isMyPerspective = isMyPerspective,
        status = null,
        createdAt = createdAt
    )

fun MyPerspectiveResponseDto.toDomainModel(): PerspectiveDetailBoard =
    PerspectiveDetailBoard(
        perspectiveId = perspectiveId,
        userTag = user.userTag,
        nickname = user.nickname.requireField("PerspectiveUserDto.nickname"),
        characterType = user.characterType,
        characterImageUrl = user.characterImageUrl,
        optionId = option.optionId,
        optionLabel = option.label,
        optionTitle = option.title,
        optionStance = option.stance,
        content = content,
        likeCount = likeCount,
        commentCount = commentCount,
        isLiked = isLiked,
        isMyPerspective = true,
        status = status,
        createdAt = createdAt
    )

fun PerspectiveLikeCountResponseDto.toDomainModel(): PerspectiveLikeCountBoard =
    PerspectiveLikeCountBoard(
        perspectiveId = perspectiveId,
        likeCount = likeCount
    )

fun PerspectiveCreateResponseDto.toDomainModel(): PerspectiveStatusBoard =
    PerspectiveStatusBoard(
        perspectiveId = perspectiveId,
        status = status,
        createdAt = createdAt
    )

fun PerspectiveUpdateResponseDto.toDomainModel(): PerspectiveUpdateBoard =
    PerspectiveUpdateBoard(
        perspectiveId = perspectiveId,
        content = content,
        updatedAt = updatedAt
    )

fun PerspectiveLikeToggleResponseDto.toDomainModel(): PerspectiveLikeToggleBoard =
    PerspectiveLikeToggleBoard(
        perspectiveId = perspectiveId,
        likeCount = likeCount,
        isLiked = isLiked
    )