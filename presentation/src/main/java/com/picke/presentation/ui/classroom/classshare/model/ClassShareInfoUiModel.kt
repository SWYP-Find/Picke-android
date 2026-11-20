package com.picke.presentation.ui.classroom.classshare.model

import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.presentation.util.parseServerDateTime
import java.time.LocalDateTime

data class ClassShareInfoUiModel(
    val className: String,
    val deadline: LocalDateTime?,
    val code: String
) {
    val hasDeadline: Boolean
        get() = deadline != null
}

fun ClassDetailBoard.toUiModel() = ClassShareInfoUiModel(
    className = name,
    deadline = parseServerDateTime(closeAt),
    code = inviteCode
)