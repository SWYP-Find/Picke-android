package com.picke.presentation.ui.classroom.classmyopinion.model

import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard

data class ClassMyPerspectiveUiModel(
    val perspectiveId: Long,
    val optionTitle: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int
)

fun PerspectiveDetailBoard.toUiModel() = ClassMyPerspectiveUiModel(
    perspectiveId = perspectiveId,
    optionTitle = optionTitle,
    content = content,
    likeCount = likeCount,
    commentCount = commentCount
)