package com.picke.presentation.ui.my.content.model

sealed interface ContentActivityUiEvent {
    data object ShowLoadFailedToast : ContentActivityUiEvent
}