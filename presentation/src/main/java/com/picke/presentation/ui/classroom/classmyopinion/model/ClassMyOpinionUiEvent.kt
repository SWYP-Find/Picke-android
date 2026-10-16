package com.picke.presentation.ui.classroom.classmyopinion.model

sealed interface ClassMyOpinionUiEvent {
    data object NavigateBack : ClassMyOpinionUiEvent
}