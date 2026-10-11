package com.picke.presentation.ui.classroom.classjoin.model

sealed interface ClassJoinUiEvent {
    data object NavigateBack : ClassJoinUiEvent
    data object NavigateToMyClass : ClassJoinUiEvent
}