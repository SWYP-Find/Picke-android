package com.picke.presentation.ui.classroom.myclass.model

import com.picke.domain.feature.classroom.model.MyClassBoard
import com.picke.presentation.ui.classroom.model.ClassRole
import com.picke.presentation.util.parseServerDateTime
import java.time.LocalDateTime

data class MyClassUiModel(
    val id: Long,
    val title: String,
    val topic: String,
    val isInProgress: Boolean,
    val activeMemberCount: Int,
    val deadline: LocalDateTime?,
    val isOperator: Boolean = false
)

fun MyClassBoard.toUiModel() = MyClassUiModel(
    id = classId,
    title = name,
    topic = battleTitle,
    isInProgress = !closed,
    activeMemberCount = activeMemberCount,
    deadline = parseServerDateTime(closeAt),
    isOperator = ClassRole.from(myRole).isOwner
)