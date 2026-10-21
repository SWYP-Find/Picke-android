package com.picke.presentation.ui.comment.model

import com.picke.domain.feature.comment.model.CommentBoard
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.presentation.util.toRelativeTimeText

data class CommentUiModel(
    val commentId: Long,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val optionId: Long = 0L,
    val content: String,
    val timeAgo: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val replyCount: Int = 0
)

fun CommentBoard.toUiModel() = CommentUiModel(
    commentId = commentId,
    profileImageUrl = user.characterImageUrl,
    nickname = user.nickname,
    stance = stance,
    content = content,
    timeAgo = createdAt.toRelativeTimeText(),
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine
)

fun PerspectiveDetailBoard.toUiModel() = CommentUiModel(
    commentId = perspectiveId,
    profileImageUrl = characterImageUrl,
    nickname = nickname,
    stance = optionTitle,
    optionId = optionId,
    content = content,
    timeAgo = createdAt.toRelativeTimeText(),
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine,
    replyCount = commentCount
)