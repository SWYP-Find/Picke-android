package com.picke.presentation.ui.classroom.classdebate.model

sealed interface ClassDebateUiAction {
    sealed interface StateOnly : ClassDebateUiAction

    sealed interface SideEffectOnly : ClassDebateUiAction

    data object BackClick : SideEffectOnly

    data class QuickReplyClick(val reply: String) : StateOnly

    data class InputChange(val input: String) : StateOnly

    data object SendClick : ClassDebateUiAction

    data object FinalVoteClick : SideEffectOnly

    data object FinalVoteDialogReplayClick : StateOnly

    data object FinalVoteDialogVoteClick : ClassDebateUiAction
}