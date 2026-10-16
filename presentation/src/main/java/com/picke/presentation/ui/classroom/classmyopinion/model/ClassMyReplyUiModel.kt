package com.picke.presentation.ui.classroom.classmyopinion.model

data class ClassMyReplyUiModel(
    val replyId: Long,
    val parentAuthorName: String,
    val parentStance: String,
    val parentContent: String,
    val authorName: String,
    val content: String
)