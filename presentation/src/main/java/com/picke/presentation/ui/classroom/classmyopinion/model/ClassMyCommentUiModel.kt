package com.picke.presentation.ui.classroom.classmyopinion.model

data class ClassMyCommentUiModel(
    val commentId: Long,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val replyCount: Int
)