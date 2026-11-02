package com.picke.presentation.ui.classroom.classmyopinion.model

import com.picke.domain.feature.classroom.model.ClassResultMyActivityCommentBoard

data class ClassMyReplyUiModel(
    val replyId: Long,
    val perspectiveTitle: String,
    val authorName: String,
    val content: String
)

fun ClassResultMyActivityCommentBoard.toUiModel(authorName: String) = ClassMyReplyUiModel(
    replyId = commentId,
    perspectiveTitle = perspectiveTitle,
    authorName = authorName,
    content = content
)