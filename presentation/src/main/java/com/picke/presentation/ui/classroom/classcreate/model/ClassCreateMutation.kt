package com.picke.presentation.ui.classroom.classcreate.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

internal sealed interface ClassCreateMutation {
    data class BattlesLoaded(val battles: List<ClassBattleUiModel>) : ClassCreateMutation
    data class DeadlineReset(val deadline: LocalDateTime) : ClassCreateMutation
}