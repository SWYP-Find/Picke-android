package com.picke.presentation.ui.my.philosopher.model

sealed interface PhilosopherTypeUiEvent {
    data object ShowLoadFailedToast : PhilosopherTypeUiEvent
}