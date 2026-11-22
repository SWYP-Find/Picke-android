package com.picke.presentation.ui.classroom.classmyopinion.model

import com.picke.domain.feature.classroom.model.ClassResultMyActivityCommentBoard

data class ClassMyCommentUiModel(
    val commentId: Long,
    val perspectiveTitle: String,
    val authorName: String,
    val content: String
)

fun ClassResultMyActivityCommentBoard.toUiModel(authorName: String) = ClassMyCommentUiModel(
    commentId = commentId,
    perspectiveTitle = perspectiveTitle,
    authorName = authorName,
    content = content
)