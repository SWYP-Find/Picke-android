package com.picke.presentation.ui.vote.model

sealed interface VoteUiEvent {
    data object ShowLoadFailedToast : VoteUiEvent

    data object ShowActionFailedToast : VoteUiEvent
}