package com.picke.presentation.ui.classroom.classedit.model

sealed interface ClassEditUiEvent {

    data object NavigateBack : ClassEditUiEvent

    data object NavigateBackAfterUpdated : ClassEditUiEvent

    data object ShowLoadFailedToast : ClassEditUiEvent

    data object ShowActionFailedToast : ClassEditUiEvent
}