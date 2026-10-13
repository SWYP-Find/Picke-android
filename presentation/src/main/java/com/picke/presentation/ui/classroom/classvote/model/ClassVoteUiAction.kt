package com.picke.presentation.ui.classroom.classvote.model

sealed interface ClassVoteUiAction {
    data object BackClick : ClassVoteUiAction
    data class VoteClick(val optionId: String) : ClassVoteUiAction
}