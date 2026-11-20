package com.picke.presentation.ui.home.model

sealed interface HomeUiEvent {
    data object ShowLoadFailedToast : HomeUiEvent
}