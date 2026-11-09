package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.presentation.ui.classroom.model.ClassRole
import com.picke.presentation.util.parseServerDateTime
import java.time.LocalDateTime

data class ClassDetailInfoUiModel(
    val isOperator: Boolean,
    val isInProgress: Boolean,
    val title: String,
    val memberCount: Int,
    val deadline: LocalDateTime?
)

fun ClassDetailBoard.toUiModel() = ClassDetailInfoUiModel(
    isOperator = ClassRole.from(myRole).isOwner,
    isInProgress = !closed,
    title = name,
    memberCount = memberCount,
    deadline = parseServerDateTime(closeAt)
)