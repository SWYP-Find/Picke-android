package com.picke.presentation.ui.classroom.classcreate.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDate
import java.time.LocalDateTime

internal sealed interface ClassCreateMutation {

    sealed interface RequestFinished : ClassCreateMutation

    data class RequestFailed(val errorDialog: ClassCreateErrorDialog) : RequestFinished

    data class TicketEndDateLoaded(val ticketEndDate: LocalDate?) : ClassCreateMutation

    data class BattlesLoaded(val battles: List<ClassBattleUiModel>) : RequestFinished

    data class SettingInvalid(
        val isClassNameInvalid: Boolean,
        val isDeadlineOutOfRange: Boolean
    ) : RequestFinished

    data class TopicDraftCreated(val draftId: Long) : ClassCreateMutation

    data class TopicsSuggested(
        val batchNo: Int,
        val battles: List<ClassBattleUiModel>
    ) : RequestFinished

    data class TopicClarificationNeeded(val message: String?) : RequestFinished

    data object TopicSuggestLimitReached : RequestFinished

    data class DeadlineReset(val deadline: LocalDateTime) : ClassCreateMutation
}