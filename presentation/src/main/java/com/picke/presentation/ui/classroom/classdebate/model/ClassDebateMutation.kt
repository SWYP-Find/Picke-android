package com.picke.presentation.ui.classroom.classdebate.model

internal sealed interface ClassDebateMutation {
    data class DebateLoaded(
        val battleId: Long,
        val battleTitle: String,
        val myChoice: String,
        val opponentName: String,
        val opponentImageUrl: String?,
        val messages: List<ClassDebateMessageUiModel>,
        val quickReplies: List<String>
    ) : ClassDebateMutation

    data class OpponentReplied(val messages: List<ClassDebateMessageUiModel>) : ClassDebateMutation
}
