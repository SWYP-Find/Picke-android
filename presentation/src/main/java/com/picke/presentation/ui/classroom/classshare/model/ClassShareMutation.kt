package com.picke.presentation.ui.classroom.classshare.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel

internal sealed interface ClassShareMutation {

    data class ClassShareLoaded(
        val classInfo: ClassShareInfoUiModel,
        val battle: ClassBattleUiModel
    ) : ClassShareMutation

    data object ClassShareLoadFailed : ClassShareMutation
}