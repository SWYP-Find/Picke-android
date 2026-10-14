package com.picke.presentation.ui.classroom.classdebate.model

sealed interface ClassDebateUiEvent {
    data object NavigateBack : ClassDebateUiEvent

    data class NavigateToPerspective(val classId: Long, val battleId: Long) : ClassDebateUiEvent
}