package com.picke.presentation.ui.classroom.classcreate.model

sealed interface ClassCreateUiEvent {
    data object NavigateBack : ClassCreateUiEvent
    data class NavigateToShare(val classId: Long) : ClassCreateUiEvent
}