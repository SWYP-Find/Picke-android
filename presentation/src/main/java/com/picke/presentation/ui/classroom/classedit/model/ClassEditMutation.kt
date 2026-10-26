package com.picke.presentation.ui.classroom.classedit.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDate
import java.time.LocalDateTime

internal sealed interface ClassEditMutation {

    sealed interface RequestFinished : ClassEditMutation

    data object RequestFailed : RequestFinished

    data class ClassLoaded(
        val className: String,
        val deadline: LocalDateTime?,
        val ticketEndDate: LocalDate?
    ) : ClassEditMutation

    data class BattleLoaded(val battle: ClassBattleUiModel) : ClassEditMutation

    data class SettingInvalid(
        val isClassNameInvalid: Boolean,
        val isDeadlineOutOfRange: Boolean
    ) : RequestFinished
}