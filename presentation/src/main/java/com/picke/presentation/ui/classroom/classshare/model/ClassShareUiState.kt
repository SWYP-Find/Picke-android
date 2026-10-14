package com.picke.presentation.ui.classroom.classshare.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

data class ClassShareUiState(
    val classId: Long = 0L,
    val className: String = "",
    val deadline: LocalDateTime? = null,
    val code: String = "",
    val battle: ClassBattleUiModel? = null
) {
    val hasDeadline: Boolean
        get() = deadline != null
}