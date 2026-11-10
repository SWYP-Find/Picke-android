package com.picke.data.feature.comment.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.comment.model.CommentBoard
import com.picke.domain.feature.comment.model.CommentCreateBoard
import com.picke.domain.feature.comment.model.CommentLikeToggleBoard
import com.picke.domain.feature.comment.model.CommentPageBoard
import com.picke.domain.feature.comment.model.CommentUpdateBoard
import com.picke.domain.feature.comment.model.CommentUserBoard
import kotlinx.serialization.Serializable

// [요청 DTO] 댓글 생성 및 수정 공통
@Serializable
data class CommentRequestDto(
    val content: String?
)

// [응답 DTO] 댓글 유저 정보 공통
@Serializable
data class CommentUserDto(
    val userTag: String?,
    val nickname: String?,
    val characterType: String,
    val characterImageUrl: String?
)

// [응답 DTO] 단일 댓글 정보
@Serializable
data class CommentDto(
    val commentId: Long,
    val user: CommentUserDto,
    val stance: String?,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val createdAt: String
)

// [응답 DTO] 댓글 목록 (페이지네이션)
@Serializable
data class CommentListResponseDto(
    val items: List<CommentDto>,
    val nextCursor: String?,
    val hasNext: Boolean
)

// [응답 DTO] 댓글 생성 응답
@Serializable
data class CommentCreateResponseDto(
    val commentId: Long,
    val user: CommentUserDto,
    val stance: String?,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val createdAt: String
)

// [응답 DTO] 댓글 수정 응답
@Serializable
data class CommentUpdateResponseDto(
    val commentId: Long,
    val content: String,
    val updatedAt: String
)

// [응답 DTO] 댓글 좋아요 등록 및 취소 응답
@Serializable
data class CommentLikeToggleResponseDto(
    val perspectiveId: Long, // 명세서상 응답에 perspectiveId로 내려오고 있습니다.
    val likeCount: Int,
    val isLiked: Boolean
)

// 좋아요 Mapper
fun CommentLikeToggleResponseDto.toDomainModel() = CommentLikeToggleBoard(
    perspectiveId = perspectiveId,
    likeCount = likeCount,
    isLiked = isLiked
)

// DTO -> Domain
fun CommentUserDto.toDomainModel() = CommentUserBoard(
    userTag = userTag,
    nickname = nickname.requireField("CommentUserDto.nickname"),
    characterType = characterType,
    characterImageUrl = characterImageUrl
)

fun CommentDto.toDomainModel() = CommentBoard(
    commentId = commentId,
    user = user.toDomainModel(),
    stance = stance,
    content = content,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine,
    createdAt = createdAt
)

fun CommentListResponseDto.toDomainModel() = CommentPageBoard(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextCursor = nextCursor,
    hasNext = hasNext
)

fun CommentCreateResponseDto.toDomainModel() = CommentCreateBoard(
    commentId = commentId,
    user = user.toDomainModel(),
    stance = stance,
    content = content,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine,
    createdAt = createdAt
)

fun CommentUpdateResponseDto.toDomainModel() = CommentUpdateBoard(
    commentId = commentId,
    content = content,
    updatedAt = updatedAt
)