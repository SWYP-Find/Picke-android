package com.picke.presentation.ui.classroom.classdebate.model

sealed interface ClassDebateUiEvent {
    data object NavigateBack : ClassDebateUiEvent

    data class NavigateToFinalVote(val classId: Long) : ClassDebateUiEvent

    data object ShowLoadFailedToast : ClassDebateUiEvent
}