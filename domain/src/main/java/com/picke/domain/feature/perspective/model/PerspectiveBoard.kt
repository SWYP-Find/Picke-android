package com.picke.domain.feature.perspective.model

data class PerspectivePage(
    val items: List<PerspectiveBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class PerspectiveBoard(
    val perspectiveId: Long,
    val userTag: String?,
    val nickname: String,
    val characterType: String?,
    val characterImageUrl: String?,
    val optionId: Long,
    val optionLabel: String?,
    val optionTitle: String,
    val optionStance: String?,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isMyPerspective: Boolean,
    val createdAt: String
)

// '내 관점' 또는 '단일 관점' 조회를 위한 상세 모델
data class PerspectiveDetailBoard(
    val perspectiveId: Long,
    val userTag: String?,
    val nickname: String,
    val characterType: String?,
    val characterImageUrl: String?,
    val optionId: Long,
    val optionLabel: String?,
    val optionTitle: String,
    val optionStance: String?,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isMyPerspective: Boolean,
    val createdAt: String
)

data class MyPerspectiveBoard(
    val perspectiveId: Long,
    val userTag: String?,
    val nickname: String,
    val characterType: String?,
    val characterImageUrl: String?,
    val optionId: Long,
    val optionLabel: String?,
    val optionTitle: String,
    val optionStance: String?,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val status: String,
    val createdAt: String
)

data class PerspectiveStatusBoard(
    val perspectiveId: Long,
    val status: String,
    val createdAt: String
)

data class PerspectiveUpdateBoard(
    val perspectiveId: Long,
    val content: String,
    val updatedAt: String
)

// 좋아요 수 조회 모델
data class PerspectiveLikeCountBoard(
    val perspectiveId: Long,
    val likeCount: Long
)

// 좋아요 등록/취소 결과 모델
data class PerspectiveLikeToggleBoard(
    val perspectiveId: Long,
    val likeCount: Int,
    val isLiked: Boolean
)