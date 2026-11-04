package com.picke.presentation.ui.my.discussion.model

sealed interface DiscussionHistoryUiEvent {
    data object ShowLoadFailedToast : DiscussionHistoryUiEvent
}