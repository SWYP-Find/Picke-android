package com.picke.presentation.ui.classroom.classcreate.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDate
import java.time.LocalDateTime

internal sealed interface ClassCreateMutation {

    data class TicketEndDateLoaded(val ticketEndDate: LocalDate?) : ClassCreateMutation

    data class BattlesLoaded(val battles: List<ClassBattleUiModel>) : ClassCreateMutation

    data class SettingInvalid(
        val isClassNameInvalid: Boolean,
        val isDeadlineOutOfRange: Boolean
    ) : ClassCreateMutation

    data class DeadlineReset(val deadline: LocalDateTime) : ClassCreateMutation

    data class RequestFailed(val errorDialog: ClassCreateErrorDialog) : ClassCreateMutation
}