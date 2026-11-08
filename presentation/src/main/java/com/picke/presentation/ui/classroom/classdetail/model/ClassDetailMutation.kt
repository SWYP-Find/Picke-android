package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.presentation.ui.classroom.model.ClassBattleStep
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel

internal sealed interface ClassDetailMutation {

    sealed interface RequestFinished : ClassDetailMutation

    data object RequestFailed : RequestFinished

    data class ClassDetailLoaded(
        val classInfo: ClassDetailInfoUiModel,
        val battle: ClassBattleUiModel,
        val battleStep: ClassBattleStep,
        val canParticipate: Boolean
    ) : ClassDetailMutation

    data object ClassDetailLoadFailed : ClassDetailMutation

    data object ClassChanged : ClassDetailMutation
}