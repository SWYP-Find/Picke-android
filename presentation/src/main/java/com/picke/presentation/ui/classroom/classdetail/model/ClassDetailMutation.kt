package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.presentation.ui.classroom.model.ClassBattleStep
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

internal sealed interface ClassDetailMutation {

    sealed interface RequestFinished : ClassDetailMutation

    data object RequestFailed : RequestFinished

    data class ClassDetailLoaded(
        val isOperator: Boolean,
        val isInProgress: Boolean,
        val title: String,
        val memberCount: Int,
        val deadline: LocalDateTime?
    ) : ClassDetailMutation

    data class BattleLoaded(
        val battle: ClassBattleUiModel,
        val battleStep: ClassBattleStep,
        val canParticipate: Boolean
    ) : ClassDetailMutation

    data object ClassChanged : ClassDetailMutation
}