package com.picke.presentation.ui.classroom.classdetail.model

sealed interface ClassDetailUiEvent {
    data object NavigateBack : ClassDetailUiEvent
    data class NavigateToMember(val classId: Long) : ClassDetailUiEvent
    data class NavigateToBattle(val classId: Long) : ClassDetailUiEvent
    data class NavigateToShare(val classId: Long) : ClassDetailUiEvent
    data class NavigateToResult(val classId: Long) : ClassDetailUiEvent
    data class NavigateToEdit(val classId: Long) : ClassDetailUiEvent
}