package com.picke.presentation.ui.alarm.model

sealed interface AlarmUiEvent {

    data object ShowReadAllFailedToast : AlarmUiEvent

    data object ShowActionFailedToast : AlarmUiEvent
}