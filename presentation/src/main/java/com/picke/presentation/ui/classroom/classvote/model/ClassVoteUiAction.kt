package com.picke.presentation.ui.classroom.classvote.model

sealed interface ClassVoteUiAction {
    sealed interface SideEffectOnly : ClassVoteUiAction

    data object BackClick : SideEffectOnly

    data class VoteClick(val optionId: String) : ClassVoteUiAction
}