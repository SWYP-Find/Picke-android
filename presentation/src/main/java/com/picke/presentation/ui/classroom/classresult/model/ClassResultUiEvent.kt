package com.picke.presentation.ui.classroom.classresult.model

sealed interface ClassResultUiEvent {

    data object NavigateBack : ClassResultUiEvent

    data class NavigateToMyOpinion(val classId: Long) : ClassResultUiEvent
}