package com.picke.presentation.ui.classroom.classdebate.model

internal sealed interface ClassDebateMutation {

    sealed interface RequestFinished : ClassDebateMutation

    data class ReplyFailed(val requestKey: String) : RequestFinished

    data class DebateLoaded(
        val battleTitle: String,
        val debate: ClassDebateUiModel
    ) : ClassDebateMutation

    data class ReplySubmitted(val debate: ClassDebateUiModel) : RequestFinished
}