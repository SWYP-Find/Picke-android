package com.picke.presentation.ui.classroom.classdebate.model

internal sealed interface ClassDebateMutation {

    data class DebateLoaded(
        val battleTitle: String,
        val debate: ClassDebateUiModel
    ) : ClassDebateMutation

    data class OpponentReplied(val messages: List<ClassDebateMessageUiModel>) : ClassDebateMutation
}