package com.picke.presentation.ui.perspective.model

sealed interface PerspectiveUiEvent {

    data class NavigateNext(val battleId: Long) : PerspectiveUiEvent

    data object ShowLoadFailedToast : PerspectiveUiEvent

    data object ShowCommentRequiredToast : PerspectiveUiEvent

    data object ShowReportedToast : PerspectiveUiEvent

    data object ShowAlreadyReportedToast : PerspectiveUiEvent

    data object ShowActionFailedToast : PerspectiveUiEvent
}