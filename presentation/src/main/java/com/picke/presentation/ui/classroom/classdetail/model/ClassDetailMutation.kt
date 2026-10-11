package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

internal sealed interface ClassDetailMutation {
    data class ClassDetailLoaded(
        val isOperator: Boolean,
        val isInProgress: Boolean,
        val title: String,
        val memberCount: Int,
        val deadline: LocalDateTime?,
        val battle: ClassBattleUiModel
    ) : ClassDetailMutation
}