package com.picke.presentation.ui.alarm.model

sealed class AlarmUiEvent {
    data object ShowReadAllFailedToast : AlarmUiEvent()
}