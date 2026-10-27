package com.picke.presentation.ui.classroom.classdetail.model

sealed interface ClassDetailUiEvent {

    data object NavigateBack : ClassDetailUiEvent

    data class NavigateToMember(val classId: Long) : ClassDetailUiEvent

    data class NavigateToPreVote(val classId: Long) : ClassDetailUiEvent

    data class NavigateToScenario(val classId: Long, val battleId: Long) : ClassDetailUiEvent

    data class NavigateToDebate(val classId: Long) : ClassDetailUiEvent

    data class NavigateToFinalVote(val classId: Long) : ClassDetailUiEvent

    data class NavigateToPerspective(val classId: Long, val battleId: Long) : ClassDetailUiEvent

    data class NavigateToShare(val classId: Long) : ClassDetailUiEvent

    data class NavigateToResult(val classId: Long) : ClassDetailUiEvent

    data class NavigateToEdit(val classId: Long) : ClassDetailUiEvent

    data object NavigateBackAfterClassRemoved : ClassDetailUiEvent

    data object ShowDeadlinePassedToast : ClassDetailUiEvent

    data object ShowNotParticipatingToast : ClassDetailUiEvent

    data object ShowLoadFailedToast : ClassDetailUiEvent

    data object ShowActionFailedToast : ClassDetailUiEvent
}