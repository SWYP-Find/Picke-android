package com.picke.presentation.ui.classroom.classshare.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

internal sealed interface ClassShareMutation {
    data class ClassShareLoaded(
        val className: String,
        val deadline: LocalDateTime?,
        val code: String,
        val battle: ClassBattleUiModel
    ) : ClassShareMutation
}