package com.picke.presentation.ui.classroom.classdebate.model

internal sealed interface ClassDebateMutation {

    sealed interface RequestFinished : ClassDebateMutation

    data object RequestFailed : RequestFinished

    data class DebateLoaded(
        val battleTitle: String,
        val debate: ClassDebateUiModel
    ) : ClassDebateMutation

    data class DebateRetried(val debate: ClassDebateUiModel) : RequestFinished

    data class ReplySubmitted(val debate: ClassDebateUiModel) : RequestFinished

    data class ReplyFailed(val requestKey: String) : RequestFinished
}