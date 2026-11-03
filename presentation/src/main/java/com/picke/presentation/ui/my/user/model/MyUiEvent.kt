package com.picke.presentation.ui.my.user.model

sealed interface MyUiEvent {
    data object ShowLoadFailedToast : MyUiEvent
}