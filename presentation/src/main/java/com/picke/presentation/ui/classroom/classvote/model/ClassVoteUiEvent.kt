package com.picke.presentation.ui.classroom.classvote.model

sealed interface ClassVoteUiEvent {
    data object NavigateBack : ClassVoteUiEvent

    data class NavigateToDebate(val classId: Long) : ClassVoteUiEvent

    data class NavigateToScenario(val classId: Long, val battleId: Long) : ClassVoteUiEvent

    data class NavigateToPerspective(val classId: Long, val battleId: Long) : ClassVoteUiEvent

    data object ShowLoadFailedToast : ClassVoteUiEvent

    data object ShowActionFailedToast : ClassVoteUiEvent
}