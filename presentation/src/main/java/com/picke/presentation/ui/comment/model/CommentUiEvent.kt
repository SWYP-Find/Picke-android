package com.picke.presentation.ui.comment.model

sealed interface CommentUiEvent {

    data object NavigateBack : CommentUiEvent

    data object ShowReportedToast : CommentUiEvent

    data object ShowAlreadyReportedToast : CommentUiEvent

    data object ShowActionFailedToast : CommentUiEvent
}